package com.example.demo.entity;

public class OrderItem {
    private Long id;
    private Integer quantity;
    private MenuItem menuItem;
    private Order order;

    public Long getId() { 
        return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }

    public Integer getQuantity() { 
        return quantity; 
    }
    public void setQuantity(Integer quantity) { 
        this.quantity = quantity; 
    }

    public MenuItem getMenuItem() { 
        return menuItem; 
    }
    public void setMenuItem(MenuItem menuItem) { 
        this.menuItem = menuItem; 
    }

    public Order getOrder() { 
        return order; 
    }
    public void setOrder(Order order) { 
        this.order = order; 
    }
}
