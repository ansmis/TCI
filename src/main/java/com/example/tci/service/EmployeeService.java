package com.example.tci.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.example.tci.entity.Employee;


public interface EmployeeService {
		void createEmployee(List<Employee> employees);

		Map<String, List<Map<String, Object>>> getEligibleEmployeesForBonus(Date date);
		
}
