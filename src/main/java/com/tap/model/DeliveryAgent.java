package com.tap.model;

import java.sql.Timestamp;

public class DeliveryAgent {
    private int agentId;
    private String fullName;
    private String email;
    private String phone;
    private String passwordHash;
    private String vehicleNumber;
    private boolean available;
    private boolean verified;
    private Timestamp createdAt;

    public DeliveryAgent() {}

    public DeliveryAgent(String fullName, String email, String phone, String passwordHash, String vehicleNumber) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.passwordHash = passwordHash;
        this.vehicleNumber = vehicleNumber;
    }

    public int getAgentId() { return agentId; }
    public void setAgentId(int agentId) { this.agentId = agentId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "DeliveryAgent{agentId=" + agentId + ", fullName='" + fullName + "', email='" + email +
                "', available=" + available + "}";
    }
}
