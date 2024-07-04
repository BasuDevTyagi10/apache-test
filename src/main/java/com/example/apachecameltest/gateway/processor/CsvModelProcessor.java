package com.example.apachecameltest.gateway.processor;

import com.example.apachecameltest.gateway.dto.BankStatementCsvModel;
import com.example.apachecameltest.gateway.util.Constants;
import com.example.apachecameltest.gateway.util.CsvHelper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.List;

public class CsvModelProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        String fileContent = exchange.getIn().getBody(String.class);
        String fileName = exchange.getIn().getHeader(Constants.CURRENT_FILE_KEY, String.class);
        List<BankStatementCsvModel> bankStatementCsvModels = CsvHelper.parseCsvContentToModel(fileName, fileContent);
        exchange.getIn().setBody(bankStatementCsvModels);
    }
}
