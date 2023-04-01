package com.github.bkwak.organizer;

import java.time.LocalTime;

public class Shift {
    private final int dayOfWeek;
    private final LocalTime startTime;
    private final LocalTime endTime;

    private final String employee;

    public Shift(int dayOfWeek, LocalTime startTime, LocalTime endTime, String employee) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.employee = employee;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getEmployee() {
        return employee;
    }
}
