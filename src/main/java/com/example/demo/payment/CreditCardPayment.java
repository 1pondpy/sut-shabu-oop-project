package com.example.demo.payment;

public class CreditCardPayment extends PaymentMethod {
    private String cardNumber;

    public CreditCardPayment(double amountToPay, String cardNumber) {
        super(amountToPay);
        this.cardNumber = cardNumber;
    }

    @Override
    public void process() throws Exception {
        // ตรวจสอบเลขบัตร (จำลอง)
        if (cardNumber == null || cardNumber.length() < 16) {
            throw new Exception("หมายเลขบัตรเครดิตไม่ถูกต้อง (ต้องมี 16 หลัก)");
        }
        // ในระบบจริงจะเรียก API ธนาคารตรงนี้
    }

    @Override
    public double getChange() {
        return 0.0; // บัตรเครดิตไม่มีเงินทอน
    }

    @Override
    public double getAmountReceived() {
        return this.amountToPay;
    }
}