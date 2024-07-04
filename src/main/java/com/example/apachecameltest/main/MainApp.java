package com.example.apachecameltest.main;

import com.example.apachecameltest.gateway.routes.AWSRouteBuilder;
import com.example.localstackdev.LocalStackDevS3;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.aws2.s3.AWS2S3Component;
import org.apache.camel.component.aws2.s3.AWS2S3Configuration;
import org.apache.camel.impl.DefaultCamelContext;
import software.amazon.awssdk.services.s3.S3Client;

public class MainApp {
    public static void main(String[] args) throws Exception {


        RouteBuilder awsRouteBuilder = new AWSRouteBuilder();
        try (CamelContext camelContext = new DefaultCamelContext()) {
            // Generate test data
            LocalStackDevS3.generateTestData();

            // Configure S3 client
            S3Client s3Client = LocalStackDevS3.createLocalS3Client();

            // Set up AWS S3 component
            AWS2S3Component aws2S3Component = new AWS2S3Component();
            AWS2S3Configuration aws2S3Configuration = new AWS2S3Configuration();
            aws2S3Configuration.setAmazonS3Client(s3Client);
            aws2S3Component.setConfiguration(aws2S3Configuration);
            camelContext.addComponent("aws2-s3", aws2S3Component);

            camelContext.addRoutes(awsRouteBuilder);

            camelContext.start();
            // Get the ProducerTemplate to send messages to endpoints
            ProducerTemplate template = camelContext.createProducerTemplate();

            // Send a message to the direct:test endpoint
            template.sendBody("direct:test", "Message to direct:test");

            // Allow some time for routes to process
            Thread.sleep(5000); // Adjust as needed
            camelContext.stop();
        }


    }
}
