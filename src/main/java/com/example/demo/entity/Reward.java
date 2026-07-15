package com.example.demo.entity;

public class Reward {
    private Integer id;
    private String name;
    private Integer pointsRequired;
    private String discountType;
    private Double discountValue;

    public Integer getId() { 
        return id; 
    }
    public String getName() { 
        return name; 
    }
    public Integer getPointsRequired() { 
        return pointsRequired; 
    }
    public String getDiscountType() { 
        return discountType; 
    }
    public Double getDiscountValue() { 
        return discountValue; 
    }
   
    public void setId(Integer id) { 
        this.id = id; 
    }
    public void setName(String name) { 
        this.name = name; 
    }
    public void setPointsRequired(Integer pointsRequired) { 
        this.pointsRequired = pointsRequired; 
    }
    public void setDiscountType(String discountType) { 
        this.discountType = discountType; 
    }
    public void setDiscountValue(Double discountValue) { 
        this.discountValue = discountValue; 
    }
}
