package com.github.bkwak.organizer;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class ScheduleTest {
    @Test
    void shouldCountConflicts() {
        // given
        LocalTime pickingTime = LocalTime.of(10, 0);
        LocalTime completeBy = LocalTime.of(11, 0);
        List<LocalTime[]> workingHours = new ArrayList<>();
        workingHours.add(new LocalTime[] { LocalTime.of(8, 0), LocalTime.of(12, 0) });
        workingHours.add(new LocalTime[] { LocalTime.of(13, 0), LocalTime.of(17, 0) });
        Schedule schedule = new Schedule(workingHours, LocalTime.of(9, 0), LocalTime.of(18, 0), new ArrayList<>());
        boolean[][] mockSchedule = new boolean[][] {
                { true, false, false, true, false, false, false },
                { false, false, false, false, false, false, false },
                { false, false, false, false, false, false, false },
                { false, true, true, false, true, false, false }
        };
        schedule.setSchedule(mockSchedule);

        // when
        int conflicts = schedule.countConflicts(pickingTime, completeBy);

        // then
        assertEquals(2, conflicts);
    }

}
