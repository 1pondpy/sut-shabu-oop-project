package com.example.demo.entity;

import java.time.LocalDateTime;

public class RedemptionHistory {
    private Long id;
    private String memberPhone;
    private String rewardName;
    private Integer pointsUsed;
    private LocalDateTime redeemDate;

    public Long getId() { 
        return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }

    public String getMemberPhone() { 
        return memberPhone; 
    }
    public void setMemberPhone(String memberPhone) { 
        this.memberPhone = memberPhone; 
    }

    public String getRewardName() { 
        return rewardName; 
    }
    public void setRewardName(String rewardName) { 
        this.rewardName = rewardName; 
    }

    public Integer getPointsUsed() { 
        return pointsUsed; 
    }
    public void setPointsUsed(Integer pointsUsed) { 
        this.pointsUsed = pointsUsed; 
    }

    public LocalDateTime getRedeemDate() { 
        return redeemDate; 
    }
    public void setRedeemDate(LocalDateTime redeemDate) { 
        this.redeemDate = redeemDate; 
    }
}