package com.qaracter.frauddetection.models;

public class TransferRequest {
    private Long senderAccountId;
    private Long recipientAccountId;
    private Double amount;

    public Long getSenderAccountId() {
        return senderAccountId;
    }

    public Long getRecipientAccountId() {
        return recipientAccountId;
    }

    public Double getAmount() {
        return amount;
    }
}

