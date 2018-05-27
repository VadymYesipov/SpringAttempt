package com.aimprosoft.yesipov.db.dao;

import com.aimprosoft.yesipov.db.entity.Department;

import java.util.List;

public interface DepartmentDAO {

    void addDepartment(Department department);

    void editDepartment(Department department);

    void removeDepartment(Department department, int size);

    List<Department> departmentsList();
}
