package com.gmail.tanyehzheng.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class WorkingDayTest {

    @Test
    void testGetTotalHoursWorked() {
        List<LocalDateTime> list = new ArrayList<>(){{
            add(LocalDateTime.of(2022, 8, 1, 12, 35));
            add(LocalDateTime.of(2022, 8, 1, 17, 15));
            add(LocalDateTime.of(2022, 8, 1, 18, 14));
            add(LocalDateTime.of(2022, 8, 1, 23, 55));
        }};
        WorkingDay day = new WorkingDay(list);
        final Duration expected = Duration.ofHours(10).plusMinutes(21);
        final Duration actual = day.getTotalHoursWorked();
        assertEquals(expected, actual);
    }

    @Test
    void testIsOnLeave() {
        WorkingDay day = new WorkingDay(null);
        final Duration expected = Duration.ZERO;
        final Duration actual = day.getTotalHoursWorked();
        assertEquals(expected, actual);
    }
}
