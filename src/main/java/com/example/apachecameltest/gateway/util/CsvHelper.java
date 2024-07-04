package com.example.apachecameltest.gateway.util;

import com.example.apachecameltest.gateway.dto.BankStatementCsvModel;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CsvHelper {

    public static List<BankStatementCsvModel> parseCsvContentToModel(String fileName, String fileContent) {
        List<BankStatementCsvModel> bankStatementCsvModels = new ArrayList<>();
        try(CSVParser csvParser = new CSVParser(new StringReader(fileContent), CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
            Map<String, Integer> headerMap = csvParser.getHeaderMap();
            if (headerMap == null) {
                throw new IOException("CSV file has no headers row.");
            }

            for (CSVRecord csvRecord : csvParser) {
                BankStatementCsvModel bankStatementCsvModel = new BankStatementCsvModel();
                bankStatementCsvModel.setBzAccountNumber(csvRecord.get(BankStatementCsvFileHeaders.BZ_ACCOUNT_NUMBER.getHeader()));
                bankStatementCsvModel.setBankReference(csvRecord.get(BankStatementCsvFileHeaders.BANK_REFERENCE.getHeader()));
                bankStatementCsvModel.setBankBookDate(csvRecord.get(BankStatementCsvFileHeaders.BANK_BOOK_DATE.getHeader()));
                bankStatementCsvModel.setPayeeAccountNumber(csvRecord.get(BankStatementCsvFileHeaders.PAYEE_ACCOUNT_NUMBER.getHeader()));
                bankStatementCsvModel.setPayeeName(csvRecord.get(BankStatementCsvFileHeaders.PAYEE_NAME.getHeader()));
                bankStatementCsvModel.setBrokerCode(csvRecord.get(BankStatementCsvFileHeaders.BROKER_CODE.getHeader()));
                bankStatementCsvModel.setPaymentAmount(csvRecord.get(BankStatementCsvFileHeaders.PAYMENT_AMOUNT.getHeader()));
                bankStatementCsvModel.setCurrencyType(csvRecord.get(BankStatementCsvFileHeaders.CURRENCY_TYPE.getHeader()));
                bankStatementCsvModel.setExchangeRate(csvRecord.get(BankStatementCsvFileHeaders.EXCHANGE_RATE.getHeader()));
                bankStatementCsvModel.setPolicyNumber(csvRecord.get(BankStatementCsvFileHeaders.POLICY_NUMBER.getHeader()));
                bankStatementCsvModel.setFileName(fileName);
                bankStatementCsvModels.add(bankStatementCsvModel);
            }
        } catch (IOException exception) {
            System.out.println(exception);
        }
        return bankStatementCsvModels;
    }

    private enum BankStatementCsvFileHeaders {
        BZ_ACCOUNT_NUMBER("bzAccountNumber"),
        BANK_REFERENCE("bankReference"),
        BANK_BOOK_DATE("bankBookDate"),
        PAYEE_ACCOUNT_NUMBER("payeeAccountNumber"),
        PAYEE_NAME("payeeName"),
        BROKER_CODE("brokerCode"),
        PAYMENT_AMOUNT("paymentAmount"),
        CURRENCY_TYPE("currencyType"),
        EXCHANGE_RATE("exchangeRate"),
        POLICY_NUMBER("policyNumber");

        private final String header;

        BankStatementCsvFileHeaders(String header) {
            this.header = header;
        }

        public String getHeader() {
            return header;
        }
    }

    // Helper function to convert a list of objects to a CSV string
    public static String parseModelObjectToCsv(List<BankStatementCsvModel> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            return "";
        }

        // Get headers from the class fields
        String headers = getHeaders();

        // Convert each object to a CSV row
        List<String> rows = dataList.stream()
                .map(BankStatementCsvModel::toCsvString)
                .collect(Collectors.toList());

        // Join headers and rows with newlines
        return headers + "\n" + String.join("\n", rows);
    }

    // Helper function to dynamically get headers from class fields
    private static String getHeaders() {
        Field[] fields = BankStatementCsvModel.class.getDeclaredFields();
        StringBuilder headers = new StringBuilder();

        for (Field field : fields) {
            if (!headers.isEmpty()) {
                headers.append(",");
            }
            headers.append(field.getName());
        }

        return headers.toString();
    }
}
