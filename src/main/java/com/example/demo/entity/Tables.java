package com.example.demo.entity;

public class Tables {
    private Integer id;
    private String tableNo;
    private String zone;
    private Integer seatCount;

    
    public Integer getId() { 
        return id; 
    }
    public String getTableNo() { 
        return tableNo; 
    }
    public String getZone() { 
        return zone; 
    }
    public Integer getSeatCount() { 
        return seatCount; 
    }

    
    public void setId(Integer id) { 
        this.id = id; 
    }
    public void setTableNo(String tableNo) { 
        this.tableNo = tableNo; 
    }
    public void setZone(String zone) { 
        this.zone = zone; 
    }
    public void setSeatCount(Integer seatCount) { 
        this.seatCount = seatCount; 
    }
}