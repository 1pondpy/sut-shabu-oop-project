package com.example.demo.payment;

public class PromptPayPayment extends PaymentMethod {
    
    public PromptPayPayment(double amountToPay) {
        super(amountToPay);
    }

    @Override
    public void process() throws Exception {
        // จำลองว่าสแกนสำเร็จ
    }

    @Override
    public double getChange() {
        return 0.0;
    }

    @Override
    public double getAmountReceived() {
        return this.amountToPay;
    }
}