package com.github.bkwak.organizer;

import java.util.Comparator;

public class OrderCompleteByComparator implements Comparator<Order> {
    @Override
    public int compare(Order o1, Order o2) {
        return o1.getCompleteBy().compareTo(o2.getCompleteBy());
    }
}
