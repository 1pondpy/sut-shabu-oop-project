package com.example.demo.model;

public class ShabuTable {
    private String number;
    private String status;
    private int pax;
    private String time;
    private String alertMsg;
    private String phone;
    private String soup;
    private String startTime;

    public ShabuTable(String number, String status, int pax, String time, String alertMsg) {
        this.number = number;
        this.status = status;
        this.pax = pax;
        this.time = time;
        this.alertMsg = alertMsg;
    }

    public String getNumber() { 
        return number; 
    }
    public String getStatus() { 
        return status; 
    }
    public int getPax() { 
        return pax; 
    }
    public String getTime() { 
        return time; 
    }
    public String getAlertMsg() { 
        return alertMsg; 
    }
    public String getPhone() { 
        return phone; 
    }
    public String getSoup() { 
        return soup; 
    }
    public String getStartTime() { 
        return startTime; 
    }
    public void setNumber(String number) { 
        this.number = number; 
    }
    public void setStatus(String status) { 
        this.status = status; 
    }
    public void setPax(int pax) { 
        this.pax = pax; 
    }
    public void setTime(String time) { 
        this.time = time; 
    }
    public void setAlertMsg(String alertMsg) { 
        this.alertMsg = alertMsg; 
    }
    public void setPhone(String phone) { 
        this.phone = phone; 
    }
    public void setSoup(String soup) { 
        this.soup = soup; 
    }
    public void setStartTime(String startTime) { 
        this.startTime = startTime; 
    }
}
