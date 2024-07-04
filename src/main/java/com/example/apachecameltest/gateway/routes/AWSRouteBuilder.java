package com.example.apachecameltest.gateway.routes;

import com.example.apachecameltest.gateway.dto.BankStatementAPIRequest;
import com.example.apachecameltest.gateway.dto.BankStatementCsvModel;
import com.example.apachecameltest.gateway.processor.CsvModelProcessor;
import com.example.apachecameltest.gateway.util.Constants;
import com.example.apachecameltest.gateway.util.CsvHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.builder.RouteBuilder;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

public class AWSRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:test")
            .to("aws2-s3://my-local-bucket-1?operation=listObjects")
            .convertBodyTo(List.class)
            .log("Listing S3 objects: ${body}")
            .split(body())
                .filter(exchange -> {
                    S3Object s3Object = exchange.getIn().getBody(S3Object.class);
                    return s3Object.key().endsWith(".csv");
                })
                .process(exchange -> {
                    S3Object s3Object = exchange.getIn().getBody(S3Object.class);
                    exchange.getIn().setHeader(Constants.CURRENT_FILE_KEY, s3Object.key());
                })
                .pollEnrich()
                    .simple("aws2-s3://my-local-bucket-1?operation=getObject&fileName=${in.headers."+Constants.CURRENT_FILE_KEY+"}&deleteAfterRead=false")
                    .aggregationStrategy((oldExchange, newExchange) -> {
                        if (newExchange == null) {
                            return oldExchange;
                        }
                        newExchange.getIn().setHeader(Constants.CURRENT_FILE_KEY, oldExchange.getIn().getHeader(Constants.CURRENT_FILE_KEY, String.class));
                        return newExchange;
                    })
                .process(new CsvModelProcessor())
                .log("${in.headers."+Constants.CURRENT_FILE_KEY+"}")
                .log("Body after CsvModelProcessor: ${body}")
                .split(body())
                    .process(exchange -> {
                        BankStatementCsvModel bankStatementCsvModel = exchange.getIn().getBody(BankStatementCsvModel.class);
                        bankStatementCsvModel.setInvocationDate(new Date().toString());
                        exchange.getIn().setHeader("modelUnderProcessing", bankStatementCsvModel);
                        BankStatementAPIRequest bankStatementAPIRequest = bankStatementCsvModel.toBankStatementAPIRequest();
                        ObjectMapper mapper = new ObjectMapper();
                        String bankStatementAPIRequestPayload = mapper.writeValueAsString(bankStatementAPIRequest);
                        exchange.getIn().setBody(bankStatementAPIRequestPayload);
                    })
                    .log("Body after JSON conversion: ${body}")
                    .doTry()
                        // Make API call
                        .to("http://api.example.com/service")
                    .doCatch(UnknownHostException.class)
                        .process(exchange -> {
                            BankStatementCsvModel bankStatementCsvModel = exchange.getIn().getHeader("modelUnderProcessing", BankStatementCsvModel.class);
                            bankStatementCsvModel.setErrorMsg("Unknown Host");
                        })
                        .log("Body after failure: ${body}")
                .end()
            .log("Body after API calls: ${body}")
            .end()
        .log("Body before CSV conversion: ${body}")
        .process(exchange -> {
            List<BankStatementCsvModel> bankStatementCsvModels = exchange.getIn().getBody(List.class);
            exchange.getIn().setBody(CsvHelper.parseModelObjectToCsv(bankStatementCsvModels));
        })
        .log("Body after CSV conversion: ${body}")
        .process(exchange -> exchange.getIn().removeHeaders("CamelAwsS3*"))
        .setHeader("CamelAwsS3Key", simple("${in.headers."+Constants.CURRENT_FILE_KEY+"}"))
        .convertBodyTo(InputStream.class)
        .toD("aws2-s3://my-local-bucket-2")
        .process(exchange -> exchange.getIn().removeHeaders("CamelAwsS3*"))
        .setHeader("CamelAwsS3Key", simple("${in.headers."+Constants.CURRENT_FILE_KEY+"}"))
        .toD("aws2-s3://my-local-bucket-1?operation=deleteObject")
        .end();
    }
}
