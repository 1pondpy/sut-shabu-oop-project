package com.example.demo.dto;

public class CartItemRequest {
    private Integer id;  // รับ id อาหาร
    private Integer qty; // รับจำนวนที่สั่ง

    public Integer getId() { 
        return id; 
    }
    public void setId(Integer id) { 
        this.id = id; 
    }

    public Integer getQty() { 
        return qty; 
    }
    public void setQty(Integer qty) { 
        this.qty = qty; 
    }
}