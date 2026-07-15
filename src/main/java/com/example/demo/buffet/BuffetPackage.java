package com.example.demo.buffet;

import com.example.demo.entity.MenuItem;


public abstract class BuffetPackage {
   
    public abstract String getName();

    public abstract boolean allows(MenuItem item);

    public abstract int getPricePerPerson();
}
