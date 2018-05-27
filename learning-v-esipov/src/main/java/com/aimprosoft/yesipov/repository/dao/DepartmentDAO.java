package com.aimprosoft.yesipov.repository.dao;

import com.aimprosoft.yesipov.repository.entity.Department;
import org.springframework.data.repository.CrudRepository;


import java.util.List;

public interface DepartmentDAO {//extends CrudRepository<Department, Long> {

    void addDepartment(Department department);

    void editDepartment(Department department, Integer id);

    List<Department> departmentsList();

    void removeDepartment(Department department);
}
