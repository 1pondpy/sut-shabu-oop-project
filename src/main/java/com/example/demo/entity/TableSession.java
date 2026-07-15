package com.example.demo.entity;

import java.time.LocalDateTime;

public class TableSession {
    private Long id;
    private String tableNo;
    private LocalDateTime startTime;
    private Integer pax;
    private String status;
    private String customerName;
    private String customerPhone;
    private String soup;
    private String packageType; 
    private LocalDateTime endTime;
    private Double totalAmount;
    private String appliedRewardIds;

    public String getTableNo() { 
        return tableNo; 
    }
    public void setTableNo(String tableNo) { 
        this.tableNo = tableNo; 
    }

    public LocalDateTime getStartTime() { 
        return startTime; 
    }
    public void setStartTime(LocalDateTime startTime) { 
        this.startTime = startTime; 
    }

    public Integer getPax() { 
        return pax; 
    }
    public void setPax(Integer pax) { 
        this.pax = pax; 
    }

    public String getStatus() { 
        return status; 
    }
    public void setStatus(String status) { 
        this.status = status; 
    }

    public String getCustomerName() { 
        return customerName; 
    }
    public void setCustomerName(String customerName) { 
        this.customerName = customerName; 
    }

    public String getCustomerPhone() { 
        return customerPhone; 
    }
    public void setCustomerPhone(String customerPhone) { 
        this.customerPhone = customerPhone; 
    }

    public String getSoup() { 
        return soup; 
    }
    public void setSoup(String soup) { 
        this.soup = soup; 
    }

    public String getPackageType() { 
        return packageType; 
    }
    public void setPackageType(String packageType) { 
        this.packageType = packageType; 
    }

    public Long getId() { 
        return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }

    public LocalDateTime getEndTime() { 
        return endTime; 
    }
    public void setEndTime(LocalDateTime endTime) { 
        this.endTime = endTime; 
    }

    public Double getTotalAmount() { 
        return totalAmount; 
    }
    public void setTotalAmount(Double totalAmount) { 
        this.totalAmount = totalAmount; 
    }

    public String getAppliedRewardIds() { 
        return appliedRewardIds; 
    }
    public void setAppliedRewardIds(String appliedRewardIds) { 
        this.appliedRewardIds = appliedRewardIds; 
    }
}
