package com.github.bkwak.organizer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // sprawdzenie, czy podano argumenty wiersza poleceń
        if (args.length != 2) {
            System.err.println("Usage: java -jar app.jar <store_file> <orders_file>");
            System.exit(1);
        }

        // wczytanie pliku store.json
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

        // wczytanie pliku orders.json
        File ordersFile = new File(args[1]);
        List<Order> orders;
        try {
            orders = objectMapper.readValue(ordersFile, new TypeReference<List<Order>>() {});
        } catch (IOException e) {
            System.err.println("Error reading orders file: " + e.getMessage());
            System.exit(1);
            return;
        }

        // sortowanie zamówień według czasu completeBy
        Collections.sort(orders, new OrderCompleteByComparator());

        // sprawdzenie poprawności wczytanych danych
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



        // przypisanie zamówień do pracowników
        if (orders.size() >= store.getPickers().size()) {
            // przypisanie co najmniej jednego zamówienia dla każdego pracownika
            int ordersPerPicker = orders.size() / store.getPickers().size();
            int remainingOrders = orders.size() % store.getPickers().size();

            // przypisanie zamówień
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

                    // wyznaczanie harmonogramu pracy dla danego pracownika
                    Schedule schedule = new Schedule((List<LocalTime[]>) picker.getWorkingHours().get(0), store.getPickingStartTime(), store.getPickingEndTime(), orders);
                    // wyznaczanie konfliktów dla zamówienia
                    int conflicts = schedule.countConflicts(order.getPickingTime(), order.getCompleteBy());
                    // aktualizacja liczby konfliktów dla pracownika
                    List<LocalTime> conflictsList = new ArrayList<>(); // utworzenie pustej listy
                    for (int j = 0; j < conflicts; j++) {
                        conflictsList.add(LocalTime.of(0, 0, 0));
                        // dodanie elementu do listy, w tym przypadku dodajemy czas trwania konfliktu równy 0 sekund
                    }
                    picker.getConflicts().addAll(conflictsList); //
                }
            }
        } else {
            // przypisanie każdego zamówienia do innego pracownika
            int currentPicker = 0;
            for (Order order : orders) {
                Picker picker = store.getPickers().get(currentPicker);
                order.setPicker(picker);

                // wyznaczanie harmonogramu pracy dla danego pracownika
                Schedule schedule = new Schedule((List<LocalTime[]>) picker.getWorkingHours().get(0), store.getPickingStartTime(), store.getPickingEndTime(), orders);

                // wyznaczanie konfliktów dla zamówienia
                int conflicts = schedule.countConflicts(order.getPickingTime(), order.getCompleteBy());
                // aktualizacja liczby konfliktów dla pracownika
                picker.getConflicts().addAll(Collections.nCopies(conflicts, LocalTime.of(0, 0, 0)));

                currentPicker = (currentPicker + 1) % store.getPickers().size();
            }
        }

        // wyświetlenie wyników przypisania zamówień do pracowników
        System.out.println("Assignment of orders to pickers:");
        for (Order order : orders) {
            System.out.println("Order " + order.getOrderId() + " assigned to picker " + order.getPicker().getName());
        }




    }
}
