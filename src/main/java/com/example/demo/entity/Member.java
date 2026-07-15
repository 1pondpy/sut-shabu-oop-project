package com.example.demo.entity;

public class Member {
    private Long id;
    private String phoneNumber;
    private String name;
    private Integer points;
    private Integer gameChances;

    public Long getId() { 
        return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }

    public String getPhoneNumber() { 
        return phoneNumber; 
    }
    public void setPhoneNumber(String phoneNumber) { 
        this.phoneNumber = phoneNumber; 
    }

    public String getName() { 
        return name; 
    }
    public void setName(String name) { 
        this.name = name; 
    }

    public Integer getPoints() { 
        return points; 
    }
    public void setPoints(Integer points) { 
        this.points = points; 
    }

    public Integer getGameChances() { 
        return gameChances; 
    }
    public void setGameChances(Integer gameChances) { 
        this.gameChances = gameChances; 
    }
}
