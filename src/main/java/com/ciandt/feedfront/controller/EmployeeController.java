package com.ciandt.feedfront.controller;

import com.ciandt.feedfront.contracts.Service;
import com.ciandt.feedfront.models.Employee;

public class EmployeeController {

    private Service<Employee> service;

    public EmployeeController(){
        this.service = new EmployeeService();
    }
}
