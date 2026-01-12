package com.qaracter.frauddetection.models;

public class TransferRequest {
    private Long senderAccountId;
    private Long recipientAccountId;
    private double amount;

    public Long getSenderAccountId() {
        return senderAccountId;
    }

    public Long getRecipientAccountId() {
        return recipientAccountId;
    }

    public double getAmount() {
        return amount;
    }
}

