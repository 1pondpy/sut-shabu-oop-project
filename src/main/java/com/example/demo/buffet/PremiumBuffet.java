package com.example.demo.buffet;

import com.example.demo.entity.MenuItem;

public class PremiumBuffet extends BuffetPackage {
    private final int pricePerPerson;

    public PremiumBuffet(int pricePerPerson) { this.pricePerPerson = pricePerPerson; }

    @Override
    public String getName() { 
        return "PREMIUM"; 
    }

    @Override
    public boolean allows(MenuItem item) { 
        return true; 
    }

    @Override
    public int getPricePerPerson() { 
        return pricePerPerson; 
    }
}
