package com.tap.model;

import java.sql.Timestamp;

public class Payment {
    private int paymentId;
    private int orderId;
    private double amount;
    private String method;   // COD, UPI, CREDIT_CARD, DEBIT_CARD
    private String status;   // PENDING, SUCCESS, FAILED
    private String transactionRef;
    private Timestamp paidAt;

    public Payment() {}

    public Payment(int orderId, double amount, String method, String status, String transactionRef) {
        this.orderId = orderId;
        this.amount = amount;
        this.method = method;
        this.status = status;
        this.transactionRef = transactionRef;
    }

    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getTransactionRef() { return transactionRef; }
    public void setTransactionRef(String transactionRef) { this.transactionRef = transactionRef; }
    public Timestamp getPaidAt() { return paidAt; }
    public void setPaidAt(Timestamp paidAt) { this.paidAt = paidAt; }

    @Override
    public String toString() {
        return "Payment{paymentId=" + paymentId + ", orderId=" + orderId + ", amount=" + amount +
                ", method='" + method + "', status='" + status + "'}";
    }
}
