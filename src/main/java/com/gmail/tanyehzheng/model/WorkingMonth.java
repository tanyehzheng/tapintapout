package com.gmail.tanyehzheng.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class WorkingMonth {

    private Employee employee;
    private final Map<Integer, WorkingDay> workingDays = new TreeMap<>();

    public WorkingMonth() {
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Employee getEmployee() {
        return employee;
    }

    public WorkingDay getWorkingDay(int day) {
        return workingDays.get(day);
    }

    public WorkingDay putWorkingDay(int day, WorkingDay workingDay) {
        return workingDays.put(day, workingDay);
    }

    @Override
    public String toString() {
        return "WorkingMonth [employee=" + employee + ", workingDays=" + workingDays + "]";
    }
    
}
