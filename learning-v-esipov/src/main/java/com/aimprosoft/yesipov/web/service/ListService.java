package com.aimprosoft.yesipov.web.service;

import com.aimprosoft.yesipov.repository.dao.DepartmentDAO;
import com.aimprosoft.yesipov.repository.dao.EmployeeDAO;
import com.aimprosoft.yesipov.repository.dao.impl.MySQLDepartmentDAO;
import com.aimprosoft.yesipov.repository.entity.Department;
import com.aimprosoft.yesipov.repository.entity.Employee;
import org.apache.log4j.Logger;

import java.util.List;

public class ListService {

    private static final Logger log = Logger.getLogger(ListService.class);

    private DepartmentDAO departmentDAO;
    private EmployeeDAO employeeDAO;

    public ListService(DepartmentDAO departmentDAO, EmployeeDAO employeeDAO) {
        this.departmentDAO = departmentDAO;
        this.employeeDAO = employeeDAO;
    }

    public List<Employee> getFilteredList(Integer id) {

        List<Employee> employees = employeeDAO.filteredEmployeeList(id);
        if (employees.size() > 0) {
            log.trace("Employee size = " + employees.size());
        }
        log.debug("Command finished");

        return employees;
    }

    public List<Employee> getEmployeeList() {

        List<Employee> employees = employeeDAO.employeeList();

        log.trace("Employee size = " + employees.size());

        return employees;
    }

    public List<Department> getDepartmentList() {
        List<Department> departments = MySQLDepartmentDAO.getMySQLDepartmentDAO().departmentsList();

        log.trace("Employee size = " + departments.size());

        return departments;
    }
}
