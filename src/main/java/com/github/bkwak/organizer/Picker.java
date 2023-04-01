package com.github.bkwak.organizer;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Picker {
    private String name;
    private int id;
    private int maxOrderCapacity;

    private List<Shift> shifts;

    private List<LocalTime> conflicts;

    private List<WorkingHours> workingHours;

    public Picker(String name, int id, int maxOrderCapacity, List<Shift> shifts, List<WorkingHours> workingHours) {
        this.name = name;
        this.id = id;
        this.maxOrderCapacity = maxOrderCapacity;
        this.shifts = new ArrayList<>(shifts);
        this.conflicts = new ArrayList<>();
        this.workingHours = workingHours;
    }
    public Picker(String name) {
        this.name = name;
    }
    public Picker() {
        this.workingHours = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getMaxOrderCapacity() {
        return maxOrderCapacity;
    }

    public void setPicker(Order order) {
        if (order.getPicker() == this) {
            return;
        }

        if (order.getPicker() != null) {
            order.getPicker().removeOrder(order);
        }

        if (order.getPickingTime() != null) {
            order.setPickingTime(null);
        }

        order.setPicker(this);
        addOrder(order);
    }

    public void addOrder(Order order) {
        order.setPicker(this);
    }

    public void removeOrder(Order order) {
        order.setPicker(null);
        if (order.getPickingTime() != null) {
            order.setPickingTime(null);
        }
    }

    public boolean canPickOrder(Order order) {
        return order.getPicker() == null && order.getOrderValue().compareTo(BigDecimal.valueOf(maxOrderCapacity)) <= 0;
    }

    public void setConflicts(List<LocalTime> conflicts) {
        this.conflicts = conflicts;
    }
    public List<LocalTime> getConflicts() {
        return conflicts;
    }

    public List<WorkingHours> getWorkingHours() {
        return workingHours;
    }
}
