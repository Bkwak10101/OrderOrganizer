package com.github.bkwak.organizer;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

public class Schedule {
    private boolean[][] schedule;

    private LocalTime[] workingHours;
    private LocalTime pickingStartTime;
    private LocalTime pickingEndTime;
    private List<Order> orders;

    public Schedule(List<LocalTime[]> workingHours, LocalTime pickingStartTime, LocalTime pickingEndTime, List<Order> orders) {
        List<LocalTime> hoursList = new ArrayList<>();
        for (LocalTime[] hours : workingHours) {
            for (int i = 0; i < hours.length; i++) {
                hoursList.add(hours[i]);
            }
        }
        this.workingHours = hoursList.toArray(new LocalTime[0]);
        this.pickingStartTime = pickingStartTime;
        this.pickingEndTime = pickingEndTime;
        this.orders = orders;
    }

    public int countConflicts(LocalTime pickingTime, LocalTime completeBy) {
        int startSlot = getOrderStartSlot(pickingTime);
        int endSlot = getOrderEndSlot(completeBy);

        int conflicts = 0;
        for (int i = 0; i < schedule.length; i++) {
            boolean conflict = false;
            for (int j = startSlot; j < endSlot; j++) {
                if (schedule[i][j]) {
                    conflict = true;
                    break;
                }
            }
            if (conflict) {
                conflicts++;
            }
        }

        return conflicts;
    }

    private int getOrderStartSlot(LocalTime pickingTime) {
        for (int i = 0; i < schedule[0].length; i++) {
            if (pickingTime.isBefore(LocalTime.of(i / 2, (i % 2) * 30))) {
                return i - 1;
            }
        }
        return schedule[0].length - 1;
    }

    private int getOrderEndSlot(LocalTime completeBy) {
        for (int i = schedule[0].length - 1; i >= 0; i--) {
            if (completeBy.isAfter(LocalTime.of(i / 2, (i % 2) * 30))) {
                return i + 1;
            }
        }
        return 0;
    }

    private int getOrderStartSlot(LocalTime pickingTime, LocalTime[] workingHours, LocalTime pickingStartTime, LocalTime pickingEndTime) {
        if (pickingTime.isBefore(pickingStartTime)) {
            return 0;
        } else if (pickingTime.isAfter(pickingEndTime)) {
            return workingHours.length - 1;
        } else {
            for (int i = 1; i < workingHours.length; i++) {
                if (pickingTime.isBefore(workingHours[i])) {
                    return i - 1;
                }
            }
            return workingHours.length - 2;
        }
    }

    private int getOrderEndSlot(LocalTime completeBy, LocalTime[] workingHours, LocalTime pickingStartTime, LocalTime pickingEndTime) {
        if (completeBy.isBefore(pickingStartTime)) {
            return 0;
        } else if (completeBy.isAfter(pickingEndTime)) {
            return workingHours.length - 1;
        } else {
            for (int i = 1; i < workingHours.length; i++) {
                if (completeBy.isBefore(workingHours[i])) {
                    return i;
                }
            }
            return workingHours.length;
        }
    }
}
