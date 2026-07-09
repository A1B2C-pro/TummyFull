package com.tap.dao;

import com.tap.model.Payment;

public interface PaymentDAO {
    boolean recordPayment(Payment payment);
    Payment getPaymentByOrder(int orderId);
    boolean updatePaymentStatus(int orderId, String status);
}
