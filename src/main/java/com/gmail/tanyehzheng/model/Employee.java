package com.gmail.tanyehzheng.model;

public class Employee {

    private String name;
    private String employeeId;

    public Employee() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return this.name;
    }

    public String getEmployeeId() {
        return this.employeeId;
    }
}
