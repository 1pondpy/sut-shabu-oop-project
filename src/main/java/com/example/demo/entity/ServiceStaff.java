package com.example.demo.entity;

public class ServiceStaff extends Staff {
    public ServiceStaff(Long id, String username, String password, String name) {
        super(id, username, password, name, "Staff");
    }

    @Override
    public String getRedirectUrl() {
        return "staff/tables"; 
    }
}