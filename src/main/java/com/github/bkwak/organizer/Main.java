package com.github.bkwak.organizer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.bkwak.organizer.model.Order;
import com.github.bkwak.organizer.model.OrderCompleteByComparator;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // check if command line arguments are provided
        if (args.length != 2) {
            System.err.println("Usage: java -jar app.jar <store_file> <orders_file>");
            System.exit(1);
        }

        // read store.json file
        File storeFile = new File(args[0]);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Store store;
        try {
            store = objectMapper.readValue(storeFile, Store.class);
        } catch (IOException e) {
            System.err.println("Error reading store file: " + e.getMessage());
            System.exit(1);
            return;
        }

        // read orders.json file
        File ordersFile = new File(args[1]);
        List<Order> orders;
        try {
            orders = objectMapper.readValue(ordersFile, new TypeReference<List<Order>>() {});
        } catch (IOException e) {
            System.err.println("Error reading orders file: " + e.getMessage());
            System.exit(1);
            return;
        }

        // sort orders by completeBy time
        Collections.sort(orders, new OrderCompleteByComparator());

        // check if loaded data is valid
        if (store.getPickers().isEmpty()) {
            System.err.println("No pickers specified in the store file.");
            System.exit(1);
            return;
        }
        if (store.getPickingStartTime() == null || store.getPickingEndTime() == null) {
            System.err.println("Picking start/end time not specified in the store file.");
            System.exit(1);
            return;
        }
        if (store.getPickingStartTime().isAfter(store.getPickingEndTime())) {
            System.err.println("Picking start time is after picking end time in the store file.");
            System.exit(1);
            return;
        }
        if (orders.isEmpty()) {
            System.err.println("No orders specified in the orders file.");
            System.exit(1);
            return;
        }
        for (Order order : orders) {
            if (order.getOrderValue() == null || order.getPickingTime() == null || order.getCompleteBy() == null) {
                System.err.println("Missing attribute(s) in order " + order.getOrderId());
                System.exit(1);
                return;
            }
            if (LocalTime.parse(order.getCompleteBy().format(DateTimeFormatter.ISO_LOCAL_TIME)).isBefore(store.getPickingEndTime())) {
                System.err.println("Order " + order.getOrderId() + " must be completed after picking end time.");
                System.exit(1);
                return;
            }
        }



        // assign orders to pickers
        if (orders.size() >= store.getPickers().size()) {
            int ordersPerPicker = orders.size() / store.getPickers().size();
            int remainingOrders = orders.size() % store.getPickers().size();

            int currentOrder = 0;
            for (Picker picker : store.getPickers()) {
                int ordersToAssign = ordersPerPicker;
                if (remainingOrders > 0) {
                    ordersToAssign++;
                    remainingOrders--;
                }
                for (int i = 0; i < ordersToAssign; i++) {
                    Order order = orders.get(currentOrder++);
                    order.setPicker(picker);

                    // setting a work schedule for a given employee
                    Schedule schedule = new Schedule((List<LocalTime[]>) picker.getWorkingHours().get(0), store.getPickingStartTime(), store.getPickingEndTime(), orders);
                    // set conflicts for the order
                    int conflicts = schedule.countConflicts(order.getPickingTime(), order.getCompleteBy());
                    List<LocalTime> conflictsList = new ArrayList<>();
                    for (int j = 0; j < conflicts; j++) {
                        conflictsList.add(LocalTime.of(0, 0, 0));
                    }
                    picker.getConflicts().addAll(conflictsList); //
                }
            }
        } else {
            // assigning each order to a different employee
            int currentPicker = 0;
            for (Order order : orders) {
                Picker picker = store.getPickers().get(currentPicker);
                order.setPicker(picker);

                // setting a work schedule for a given employee
                Schedule schedule = new Schedule((List<LocalTime[]>) picker.getWorkingHours().get(0), store.getPickingStartTime(), store.getPickingEndTime(), orders);

                // determine conflicts for the order
                int conflicts = schedule.countConflicts(order.getPickingTime(), order.getCompleteBy());
                picker.getConflicts().addAll(Collections.nCopies(conflicts, LocalTime.of(0, 0, 0)));

                currentPicker = (currentPicker + 1) % store.getPickers().size();
            }
        }
        System.out.println("Assignment of orders to pickers:");
        for (Order order : orders) {
            System.out.println("Order " + order.getOrderId() + " assigned to picker " + order.getPicker().getName());
        }

    }
}
