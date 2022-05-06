package com.bepastem.models;

import com.bepastem.util.IdsGenerator;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Subscription {
    @Id
    private String subscriptionId;
    private long subscriptionId_userId;
    private String plan;
    private int price;
    @Transient
    private final IdsGenerator idsGenerator = new IdsGenerator();

    public Subscription() {
        this.subscriptionId = idsGenerator.generateStringId();
        this.plan = "free";
        this.price = 0;
    }

    public Subscription(String plan, String price) {
        this.subscriptionId = idsGenerator.generateStringId();
        this.plan = "free";
        this.price = 0;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String id) {
        this.subscriptionId = id;
    }

    public long getSubscriptionId_userId() {
        return subscriptionId_userId;
    }

    public void setSubscriptionId_userId(long subscriptionId_userId) {
        this.subscriptionId_userId = subscriptionId_userId;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "subscriptionId='" + subscriptionId + '\'' +
                ", subscriptionId_userId=" + subscriptionId_userId +
                ", plan='" + plan + '\'' +
                ", price=" + price +
                '}';
    }
}
