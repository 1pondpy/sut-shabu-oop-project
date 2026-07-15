package com.example.demo.buffet;
public class BuffetFactory {
    public static BuffetPackage of(String name) {
        // read config and construct packages with configured prices
        com.example.demo.buffet.BuffetConfigRepository repo = new com.example.demo.buffet.BuffetConfigRepository();
        com.example.demo.buffet.BuffetConfig cfg = repo.read();
        if (name == null) 
            return new StandardBuffet(cfg.getStandardPrice());
        if ("PREMIUM".equalsIgnoreCase(name)) 
            return new PremiumBuffet(cfg.getPremiumPrice());
        return new StandardBuffet(cfg.getStandardPrice());
    }
}
