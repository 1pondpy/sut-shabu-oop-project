package com.example.demo.entity;

public class KitchenStaff extends Staff {
    public KitchenStaff(Long id, String username, String password, String name) {
        super(id, username, password, name, "Kitchen Staff");
    }

    @Override
    public String getRedirectUrl() {
        return "kitchen"; 
    }
}