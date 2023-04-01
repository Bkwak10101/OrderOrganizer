package com.github.bkwak.organizer;

import java.time.LocalTime;
import java.util.List;

public class Store {
    private List<Picker> pickers;
    private LocalTime pickingStartTime;
    private LocalTime pickingEndTime;
    private List<Order> orders;
    public Store() {
    }

    public Store(List<Picker> pickers, List<Order> orders, LocalTime pickingStartTime, LocalTime pickingEndTime) {
        this.pickers = pickers;
        this.orders = orders;
        this.pickingStartTime = pickingStartTime;
        this.pickingEndTime = pickingEndTime;
    }

    public List<Picker> getPickers() {
        return pickers;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setPickers(List<Picker> pickers) {
        this.pickers = pickers;
    }

    public LocalTime getPickingStartTime() {
        return pickingStartTime;
    }

    public void setPickingStartTime(LocalTime pickingStartTime) {
        this.pickingStartTime = pickingStartTime;
    }

    public LocalTime getPickingEndTime() {
        return pickingEndTime;
    }

    public void setPickingEndTime(LocalTime pickingEndTime) {
        this.pickingEndTime = pickingEndTime;
    }
}