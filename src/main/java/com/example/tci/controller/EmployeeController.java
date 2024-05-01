package com.example.tci.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.tci.entity.Employee;
import com.example.tci.exception.ParseDateException;
import com.example.tci.service.EmployeeService;


@RestController
@RequestMapping("/tci")
public class EmployeeController {
	//to inject the EmployeeService dependency
	@Autowired
	private EmployeeService employeeService;
	
	//Build a employee rest api
	@PostMapping("/employee-bonus")
	public ResponseEntity<String> processEmployeeData(@RequestBody List<Employee> employees) {
        employeeService.createEmployee(employees);
        return ResponseEntity.ok("Employee data stored successfully");
    }
	
	@GetMapping("/employee-bonus")
	public ResponseEntity<EmployeeBonusResponse> getEligibleEmployeesForBonus(
	        @RequestParam("date") String dateString) throws ParseException {
		dateString = dateString.replaceAll("\"", "");
		Date date = null;
	    try {
	        SimpleDateFormat formatter = new SimpleDateFormat("MMM-dd-yyyy");
	        date = formatter.parse(dateString);
	    } catch (ParseDateException e) {
	        throw new ParseDateException("Invalid date format. Date should be in MMM-dd-yyyy format.");
	    }
	    Map<String, List<Map<String, Object>>> response = employeeService.getEligibleEmployeesForBonus(date);
	    EmployeeBonusResponse employeeBonusResponse = new EmployeeBonusResponse();
	    employeeBonusResponse.setErrorMessage("");
	    employeeBonusResponse.setData(convertToEmployeeBonusData(response));
	    return ResponseEntity.ok(employeeBonusResponse);
	}
	
	private List<Map<String, Object>> convertToEmployeeBonusData(Map<String, List<Map<String, Object>>> response) {
	    List<Map<String, Object>> employeeBonusData = new ArrayList<>();
	    for (Map.Entry<String, List<Map<String, Object>>> entry : response.entrySet()) {
	        Map<String, Object> currencyData = new HashMap<>();
	        currencyData.put("currency", entry.getKey());
	        currencyData.put("employees", entry.getValue());
	        employeeBonusData.add(currencyData);
	    }
	    return employeeBonusData;
	}
	
	
}
