package com.example.demo.buffet;

import com.example.demo.entity.MenuItem;

public class StandardBuffet extends BuffetPackage {
    private final int pricePerPerson;

    public StandardBuffet(int pricePerPerson) { 
        this.pricePerPerson = pricePerPerson; 
    }

    @Override
    public String getName() { 
        return "STANDARD"; 
    }

    @Override
    public boolean allows(MenuItem item) { 
        return item == null ? false : !item.isPremium(); 
    }

    @Override
    public int getPricePerPerson() { 
        return pricePerPerson; 

    }
}
