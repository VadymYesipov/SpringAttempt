package com.aimprosoft.yesipov.repository.dao;

import com.aimprosoft.yesipov.repository.entity.Employee;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmployeeDAO {//extends CrudRepository<Employee, Long> {

    void addEmployee(Employee employee);

    void editEmployee(Employee employee, Integer id);

    void removeEmployee(Employee employee);

    List<Employee> employeeList();

    List<Employee> filteredEmployeeList(Integer id);
}
