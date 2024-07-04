package com.example.apachecameltest.gateway.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Attributes {
    @JsonProperty("bzAccountNumber")
    private String bzAccountNumber;
    @JsonProperty("bankReference")
    private String bankReference;
    @JsonProperty("bankBookDate")
    private String bankBookDate;
    @JsonProperty("exchangeRate")
    private String exchangeRate;
    @JsonProperty("fileName")
    private String fileName;
    @JsonProperty("payeeAccountNumber")
    private String payeeAccountNumber;
    @JsonProperty("payeeName")
    private String payeeName;
    @JsonProperty("paymentAmount")
    private PaymentAmount paymentAmount;
    @JsonProperty("policyNumber")
    private String policyNumber;
    @JsonProperty("producerCode")
    private String producerCode;
    @JsonProperty("retryCount")
    private Integer retryCount;
    @JsonProperty("status")
    private Status status;

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

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public PaymentAmount getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(PaymentAmount paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getProducerCode() {
        return producerCode;
    }

    public void setProducerCode(String producerCode) {
        this.producerCode = producerCode;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
