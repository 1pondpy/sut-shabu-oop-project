package com.example.demo.service;

import com.example.demo.entity.Staff;
import com.example.demo.repository.StaffRepository;

public class LoginService {
    private StaffRepository staffRepo = new StaffRepository();

    public Staff login(String username, String password) {
        Staff staff = staffRepo.findByUsername(username);
        if (staff != null && staff.getPassword().equals(password)) {
            return staff; 
        }
        return null; 
    }
}