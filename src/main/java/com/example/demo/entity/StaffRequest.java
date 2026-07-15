package com.example.demo.entity;

import java.time.LocalDateTime;

public class StaffRequest {
    private Long id;
    private String tableNo;
    private String requestType;
    private LocalDateTime requestTime;
    private String status; 

    public Long getId() { 
        return id; }
    public void setId(Long id) { 
        this.id = id; 
    }

    public String getTableNo() { 
        return tableNo; 
    }
    public void setTableNo(String tableNo) { 
        this.tableNo = tableNo; 
    }

    public String getRequestType() { 
        return requestType; 
    }
    public void setRequestType(String requestType) { 
        this.requestType = requestType; 
    }

    public LocalDateTime getRequestTime() { 
        return requestTime; 
    }
    public void setRequestTime(LocalDateTime requestTime) { 
        this.requestTime = requestTime; 
    }

    public String getStatus() { 
        return status; 
    }
    public void setStatus(String status) { 
        this.status = status; 
    }
}
