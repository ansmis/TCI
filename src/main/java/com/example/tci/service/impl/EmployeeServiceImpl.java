package com.example.tci.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tci.entity.Department;
import com.example.tci.entity.Employee;
import com.example.tci.exception.ParseDateException;
import com.example.tci.repository.DepartmentRepository;
import com.example.tci.repository.EmployeeRepository;
import com.example.tci.service.EmployeeService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

//Service annotation tell spring container to create bean for this class.
@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService{
	
	@Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;
    

    @Transactional
	public void createEmployee(List<Employee> employees) {
    	
        for (Employee employee : employees) {
        	// Save department if it doesn't exist
            String departmentName = employee.getDepartment();
            Department department = departmentRepository.findByName(departmentName);
            if (department == null) {
                department = new Department();
                department.setName(departmentName);
                departmentRepository.save(department);
            }

            // Set the department for the employee
            employee.setDepartment(departmentName);

            // Save employee
            
            employeeRepository.save(employee);
        }
    }


	@Override
	public Map<String, List<Map<String, Object>>> getEligibleEmployeesForBonus(Date date) {
        List<Employee> allEmployees = employeeRepository.findAll();
        List<Employee> eligibleEmployees = filterEligibleEmployees(allEmployees, date);
        return groupByCurrency(eligibleEmployees);
    }
	private List<Employee> filterEligibleEmployees(List<Employee> employees, Date date) {
        return employees.stream()
                .filter(employee -> employee.getJoiningDate().before(date) && (employee.getExitDate() == null || employee.getExitDate().after(date)))
                .collect(Collectors.toList());
    }

    private Map<String, List<Map<String, Object>>> groupByCurrency(List<Employee> employees) {
    	return employees.stream()
                .collect(Collectors.groupingBy(Employee::getCurrency,
                        Collectors.mapping(employee -> {
                            Map<String, Object> employeeMap = new HashMap();
                            employeeMap.put("empName", employee.getEmpName());
                            employeeMap.put("amount", employee.getAmount());
                            return employeeMap;
                        }, Collectors.toList())));

    }
}
