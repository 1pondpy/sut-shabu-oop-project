package com.example.demo.entity;

import java.time.LocalDateTime;

public class PaymentRecord {
    private Long id;
    private String tableNo;
    private String customerName;
    private String memberPhone;
    private Double totalAmount; // ราคาเต็ม
    private Double discount;    // ส่วนลดรวม
    private Double netAmount;   // ยอดสุทธิ
    private String paymentMethod; // CASH, SCAN, CREDIT
    private Double amountReceived; // รับเงินมา
    private Double changeAmount;   // เงินทอน
    private LocalDateTime paymentTime;

    public Long getId() { 
        return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }
    public String getTableNo() { 
        return tableNo; 
    }
    public void setTableNo(String tableNo) { 
        this.tableNo = tableNo; 
    }
    public String getCustomerName() { 
        return customerName; 
    }
    public void setCustomerName(String customerName) { 
        this.customerName = customerName; 
    }
    public String getMemberPhone() { 
        return memberPhone; 
    }
    public void setMemberPhone(String memberPhone) { 
        this.memberPhone = memberPhone; 
    }
    public Double getTotalAmount() { 
        return totalAmount; 
    }
    public void setTotalAmount(Double totalAmount) { 
        this.totalAmount = totalAmount; 
    }
    public Double getDiscount() { 
        return discount; 
    }
    public void setDiscount(Double discount) { 
        this.discount = discount; 
    }
    public Double getNetAmount() { 
        return netAmount; 
    }
    public void setNetAmount(Double netAmount) { 
        this.netAmount = netAmount; 
    }
    public String getPaymentMethod() { 
        return paymentMethod;
    } 
    public void setPaymentMethod(String paymentMethod) { 
        this.paymentMethod = paymentMethod; 
    }
    public Double getAmountReceived() { 
        return amountReceived; 
    }
    public void setAmountReceived(Double amountReceived) { 
        this.amountReceived = amountReceived; 
    }
    public Double getChangeAmount() { 
        return changeAmount; 
    }
    public void setChangeAmount(Double changeAmount) { 
        this.changeAmount = changeAmount; 
    }
    public LocalDateTime getPaymentTime() { 
        return paymentTime; 
    }
    public void setPaymentTime(LocalDateTime paymentTime) { 
        this.paymentTime = paymentTime; 
    }
}