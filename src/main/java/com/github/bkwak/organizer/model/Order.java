package com.github.bkwak.organizer.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.bkwak.organizer.Picker;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@JsonDeserialize(using = OrderDeserializer.class)
public class Order {
    private String orderId;
    private BigDecimal orderValue;
    private LocalTime completeBy;
    private Picker picker;
    private LocalTime pickingTime;

    public Order() {
    }

    public Order(String orderId, BigDecimal orderValue, LocalTime completeBy, Picker picker, LocalTime pickingTime) {
        this.orderId = orderId;
        this.orderValue = orderValue;
        this.completeBy = completeBy;
        this.picker = picker;
        this.pickingTime = pickingTime;
    }

    // getters and setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(BigDecimal orderValue) {
        this.orderValue = orderValue;
    }

    public LocalTime getCompleteBy() {
        return completeBy;
    }

    public void setCompleteBy(LocalTime completeBy) {
        this.completeBy = completeBy;
    }

    public Picker getPicker() {
        return picker;
    }

    public void setPicker(Picker picker) {
        this.picker = picker;
    }

    public LocalTime getPickingTime() {
        return pickingTime;
    }

    public void setPickingTime(LocalTime pickingTime) {
        this.pickingTime = pickingTime;
    }
//    public LocalDateTime getEstimatedTime(Employee employee) {
//        int totalTime = 0;
//        for (Item item : items) {
//            totalTime += item.getTimeToComplete();
//        }
//        return employee.getNextFreeTime().plusMinutes(totalTime);
//    }

}


