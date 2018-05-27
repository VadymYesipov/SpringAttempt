package com.aimprosoft.yesipov.web.service;

import com.aimprosoft.yesipov.repository.dao.DepartmentDAO;
import com.aimprosoft.yesipov.repository.dao.EmployeeDAO;
import com.aimprosoft.yesipov.repository.entity.Department;
import com.aimprosoft.yesipov.repository.entity.Employee;
import org.apache.log4j.Logger;

import java.util.List;

public class AddEntityService {

    private static final Logger log = Logger.getLogger(AddEntityService.class);

    private DepartmentDAO departmentDAO;
    private EmployeeDAO employeeDAO;

    public AddEntityService(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    public AddEntityService(DepartmentDAO departmentDAO) {
        this.departmentDAO = departmentDAO;
    }

    public AddEntityService(DepartmentDAO departmentDAO, EmployeeDAO employeeDAO) {
        this.departmentDAO = departmentDAO;
        this.employeeDAO = employeeDAO;
    }

    public List<Department> execute(Department department) {

        departmentDAO.addDepartment(department);

        List<Department> departments = departmentDAO.departmentsList();

        log.debug("Command finished");

        return departments;
    }

    public List<Employee> execute(Employee employee) {

        employeeDAO.addEmployee(employee);

        List<Employee> employees = employeeDAO.employeeList();

        log.debug("Command finished");

        return employees;
    }
}
