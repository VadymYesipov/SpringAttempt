package com.aimprosoft.yesipov.web.service;

import com.aimprosoft.yesipov.repository.dao.DepartmentDAO;
import com.aimprosoft.yesipov.repository.dao.EmployeeDAO;
import com.aimprosoft.yesipov.repository.entity.Department;
import com.aimprosoft.yesipov.repository.entity.Employee;
import org.apache.log4j.Logger;

import java.util.List;

public class RemoveEntityService {

    private static final Logger log = Logger.getLogger(RemoveEntityService.class);

    private DepartmentDAO departmentDAO;
    private EmployeeDAO employeeDAO;

    public RemoveEntityService(DepartmentDAO departmentDAO, EmployeeDAO employeeDAO) {
        this.departmentDAO = departmentDAO;
        this.employeeDAO = employeeDAO;
    }

    public RemoveEntityService(DepartmentDAO departmentDAO) {
        this.departmentDAO = departmentDAO;
    }

    public RemoveEntityService(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    public List<Department> execute(Department department) {

        departmentDAO.removeDepartment(department);

        List<Department> departments = departmentDAO.departmentsList();

        log.debug("Command finished");

        return departments;
    }

    public List<Employee> execute(Employee employee) {

        employeeDAO.removeEmployee(employee);

        List<Employee> employees = employeeDAO.filteredEmployeeList(
                employee.getDepartmentByDepartmentId().getId());

        log.debug("Command finished");

        return employees;
    }
}
