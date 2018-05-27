package com.aimprosoft.yesipov.web.command.impl;

import com.aimprosoft.yesipov.web.Path;
import com.aimprosoft.yesipov.db.dao.impl.MySQLDepartmentDAO;
import com.aimprosoft.yesipov.db.dao.impl.MySQLEmployeeDAO;
import com.aimprosoft.yesipov.db.entity.Department;
import com.aimprosoft.yesipov.db.entity.Employee;
import com.aimprosoft.yesipov.web.command.Command;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class RemoveDepartmentCommand implements Command {

    private static final Logger log = Logger.getLogger(RemoveDepartmentCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.debug("Command starts");

        String errorMessage = null;
        String forward = Path.ERROR_PAGE;

        Integer id = Integer.valueOf(request.getParameter("id"));

        request.setAttribute("remove_ID", id);

        List<Department> departments = (List<Department>) request.getServletContext().getAttribute("departmentList");
        List<Employee> employees = (List<Employee>) request.getServletContext().getAttribute("employeeList");

        Department object = departments.stream()
                .filter(x -> id.equals(x.getId()))
                .findAny()
                .orElse(null);

        List<Employee> filteredEmployees = employees.stream()
                .filter(x -> id.equals(x.getDepartment().getId()))
                .collect(Collectors.toList());

        if (object == null) {
            errorMessage = "A department with such id doesn't exist";
            request.setAttribute("errorMessage", errorMessage);
            log.error("errorMessage --> " + errorMessage);
            return forward;
        } else {

            MySQLDepartmentDAO mySQLDepartmentDAO = new MySQLDepartmentDAO();

            mySQLDepartmentDAO.removeDepartment(object, departments.size());

            MySQLEmployeeDAO mySQLEmployeeDAO = new MySQLEmployeeDAO();

            employees = mySQLEmployeeDAO.employeeList();

            for (int i = 0; i < employees.size(); i++) {
                int oldId = employees.get(i).getId();
                employees.get(i).setId(i + 1);
                mySQLEmployeeDAO.editEmployee(employees.get(i).getId(), oldId);
            }

            employees = mySQLEmployeeDAO.employeeList();
            departments = mySQLDepartmentDAO.departmentsList();

            log.trace("Departments size = " + departments.size());
            log.trace("Employees size = " + employees.size());


            request.getServletContext().setAttribute("departmentList", departments);
            request.getServletContext().setAttribute("employeeList", employees);

            forward = Path.DEPARTMENTS_JSP;
        }


        log.trace("Forward address --> " + forward);
        log.debug("Controller finished, now go to forward address --> " + forward);

        log.debug("Command finished");
        return forward;
    }
}
