package com.example.apachecameltest.gateway.dto;

public class BankStatementCsvModel {
    private String bzAccountNumber;
    private String bankReference;
    private String bankBookDate;
    private String payeeAccountNumber;
    private String payeeName;
    private String brokerCode;
    private String paymentAmount;
    private String currencyType;
    private String exchangeRate;
    private String policyNumber;
    private String invocationDate;
    private String errorMsg;
    private String fileName;

    public String getBzAccountNumber() {
        return bzAccountNumber;
    }

    public void setBzAccountNumber(String bzAccountNumber) {
        this.bzAccountNumber = bzAccountNumber;
    }

    public String getBankReference() {
        return bankReference;
    }

    public void setBankReference(String bankReference) {
        this.bankReference = bankReference;
    }

    public String getBankBookDate() {
        return bankBookDate;
    }

    public void setBankBookDate(String bankBookDate) {
        this.bankBookDate = bankBookDate;
    }

    public String getPayeeAccountNumber() {
        return payeeAccountNumber;
    }

    public void setPayeeAccountNumber(String payeeAccountNumber) {
        this.payeeAccountNumber = payeeAccountNumber;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getBrokerCode() {
        return brokerCode;
    }

    public void setBrokerCode(String brokerCode) {
        this.brokerCode = brokerCode;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getInvocationDate() {
        return invocationDate;
    }

    public void setInvocationDate(String invocationDate) {
        this.invocationDate = invocationDate;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "BankStatementCsvModel{" +
                "bzAccountNumber='" + bzAccountNumber + '\'' +
                ", bankReference='" + bankReference + '\'' +
                ", bankBookDate='" + bankBookDate + '\'' +
                ", payeeAccountNumber='" + payeeAccountNumber + '\'' +
                ", payeeName='" + payeeName + '\'' +
                ", brokerCode='" + brokerCode + '\'' +
                ", paymentAmount='" + paymentAmount + '\'' +
                ", currencyType='" + currencyType + '\'' +
                ", exchangeRate='" + exchangeRate + '\'' +
                ", policyNumber='" + policyNumber + '\'' +
                ", invocationDate='" + invocationDate + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }

    public BankStatementAPIRequest toBankStatementAPIRequest() {
        BankStatementAPIRequest bankStatementAPIRequest = new BankStatementAPIRequest();
        Attributes attributes = new Attributes();
        attributes.setBzAccountNumber(this.bzAccountNumber);
        attributes.setBankReference(this.bankReference);
        attributes.setBankBookDate(this.bankBookDate);

        if (this.exchangeRate != null && !this.exchangeRate.isEmpty()) {
            attributes.setExchangeRate(this.exchangeRate);
        }

        attributes.setFileName(this.fileName);
        attributes.setPayeeAccountNumber(this.payeeAccountNumber);
        attributes.setPayeeName(this.payeeName);

        PaymentAmount paymentAmount = new PaymentAmount();
        paymentAmount.setAmount(this.paymentAmount);
        paymentAmount.setCurrency(this.currencyType);
        attributes.setPaymentAmount(paymentAmount);

        if (this.policyNumber != null && !this.policyNumber.isEmpty()) {
            attributes.setPolicyNumber(this.policyNumber);
        }

        attributes.setProducerCode(this.brokerCode);
        attributes.setRetryCount(0);

        Status status = new Status();
        status.setCode("InProgress");
        attributes.setStatus(status);

        Data data = new Data();
        data.setAttributes(attributes);

        bankStatementAPIRequest.setData(data);
        return bankStatementAPIRequest;
    }

    public String toCsvString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                bzAccountNumber, bankReference, bankBookDate, payeeAccountNumber,
                payeeName, brokerCode, paymentAmount, currencyType, exchangeRate,
                policyNumber, invocationDate, errorMsg, fileName);
    }

}
