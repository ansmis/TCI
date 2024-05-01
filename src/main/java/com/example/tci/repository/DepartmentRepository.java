package com.example.tci.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tci.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long>{
	 Department findByName(String name);

}
