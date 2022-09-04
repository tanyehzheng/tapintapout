package com.gmail.tanyehzheng.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class WorkingDay {

    public static WorkingDay ON_LEAVE = new WorkingDay(null);
    private List<LocalDateTime> tapInTapOut;

    public WorkingDay(List<LocalDateTime> tapInTapOut) {
        this.tapInTapOut = tapInTapOut == null ? Collections.emptyList() : tapInTapOut;
    }

    public boolean isOnLeave() {
        return this.tapInTapOut.isEmpty();
    }

    public boolean hasError() {
        //every tap in must be paired with a tap out
        return this.tapInTapOut.size() % 2 != 0;
    }

    public Duration getTotalHoursWorked() {
        if (isOnLeave()) {
            return Duration.ZERO;
        }
        if (hasError()){
            return Duration.ZERO;
        }
        Duration sum = Duration.ZERO;
        for (int i = 0; i < tapInTapOut.size(); i += 2) {
            sum = sum.plus(Duration.between(tapInTapOut.get(i), tapInTapOut.get(i + 1)));
        }
        return sum;
    }

    @Override
    public String toString() {
        return "WorkingDay [tapInTapOut=" + tapInTapOut + "]";
    }
}
