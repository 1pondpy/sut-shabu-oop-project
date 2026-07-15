package com.example.demo.payment;

public class CashPayment extends PaymentMethod {
    private double cashReceived; 
    private double change;       

    public CashPayment(double amountToPay, double cashReceived) {
        super(amountToPay);
        this.cashReceived = cashReceived;
    }

    @Override
    public void process() throws Exception {
        // เช็คว่าเงินพอไหม และคำนวณเงินทอน
        if (cashReceived < amountToPay) {
            throw new Exception("ยอดเงินไม่พอชำระ! (ขาดอีก " + (amountToPay - cashReceived) + " บาท)");
        }
        this.change = cashReceived - amountToPay;
    }

    @Override
    public double getChange() {
        return this.change;
    }

    @Override
    public double getAmountReceived() {
        return this.cashReceived;
    }
}