package com.aimprosoft.yesipov.web.service;

import com.aimprosoft.yesipov.repository.dao.DepartmentDAO;
import com.aimprosoft.yesipov.repository.dao.EmployeeDAO;
import com.aimprosoft.yesipov.repository.entity.Department;
import com.aimprosoft.yesipov.repository.entity.Employee;
import org.apache.log4j.Logger;

import java.util.List;

public class EditEntityService {

    private static final Logger log = Logger.getLogger(EditEntityService.class);

    private DepartmentDAO departmentDAO;
    private EmployeeDAO employeeDAO;

    public EditEntityService(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    public EditEntityService(DepartmentDAO departmentDAO) {
        this.departmentDAO = departmentDAO;
    }

    public EditEntityService(DepartmentDAO departmentDAO, EmployeeDAO employeeDAO) {

        this.departmentDAO = departmentDAO;
        this.employeeDAO = employeeDAO;
    }

    public List<Department> execute(Department department, Integer id) {

        departmentDAO.editDepartment(department, id);

        List<Department> departments = departmentDAO.departmentsList();

        log.debug("Command finished");

        return departments;
    }

    public List<Employee> execute(Employee employee, Integer id) {

        employeeDAO.editEmployee(employee, id);

        List<Employee> employees = employeeDAO.employeeList();

        log.debug("Command finished");

        return employees;
    }
}
