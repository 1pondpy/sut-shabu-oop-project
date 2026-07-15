package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private Long id;
    private LocalDateTime orderTime;
    private String status;
    private TableSession session;
    private List<OrderItem> orderItems;

    public Long getId() { 
        return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }

    public LocalDateTime getOrderTime() { 
        return orderTime; 
    }
    public void setOrderTime(LocalDateTime orderTime) { 
        this.orderTime = orderTime; 
    }

    public String getStatus() { 
        return status; 
    }
    public void setStatus(String status) { 
        this.status = status; 
    }

    public TableSession getSession() { 
        return session; 
    }
    public void setSession(TableSession session) { 
        this.session = session; 
    }

    public List<OrderItem> getOrderItems() { 
        return orderItems; 
    }
    public void setOrderItems(List<OrderItem> orderItems) { 
        this.orderItems = orderItems; 
    }
}
