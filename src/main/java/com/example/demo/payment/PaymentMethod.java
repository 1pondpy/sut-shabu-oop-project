package com.example.demo.payment;

public abstract class PaymentMethod {
    protected double amountToPay; 

    public PaymentMethod(double amountToPay) {
        this.amountToPay = amountToPay;
    }
    public abstract void process() throws Exception;
    public abstract double getChange();
    public abstract double getAmountReceived();
}