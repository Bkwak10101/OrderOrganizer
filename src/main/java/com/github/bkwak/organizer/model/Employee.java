// TODO: Fix the employee class and make the schedule logic to work
//package com.github.bkwak.organizer.model;
//
//import com.github.bkwak.organizer.model.Order;
//
//import java.time.LocalTime;
//import java.util.ArrayList;
//import java.util.List;
//
//public class Employee {
//    private String id;
//    private List<Order> orders;
//    private LocalTime nextAvailableTime;
//
//    public Employee(String id) {
//        this.id = id;
//        this.orders = new ArrayList<>();
//        this.nextAvailableTime = LocalTime.of(8, 0); // start work at 8:00 AM
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public List<Order> getOrders() {
//        return orders;
//    }
//
//    public LocalTime getNextAvailableTime() {
//        return nextAvailableTime;
//    }
//
//    public void addOrder(Order order) {
//        orders.add(order);
//        // update the next available time
//        nextAvailableTime = nextAvailableTime.plusMinutes(order.getEstimatedTime());
//    }
//
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        sb.append("Employee ").append(id).append(":");
//        for (Order order : orders) {
//            sb.append("\n - Order ").append(order.getOrderId())
//                    .append(", start time: ").append(order.getPickingTime());
//        }
//        return sb.toString();
//    }
//}
