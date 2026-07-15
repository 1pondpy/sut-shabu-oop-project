package com.example.demo.entity;

public class Manager extends Staff {
    public Manager(Long id, String username, String password, String name) {
        
        super(id, username, password, name, "Manager");
    }

    @Override
    public String getRedirectUrl() {
        return "dashboard"; 
    }
}