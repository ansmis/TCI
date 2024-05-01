package com.example.tci.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tci.entity.Employee;


public interface EmployeeRepository extends JpaRepository<Employee, Long>{
	

}
