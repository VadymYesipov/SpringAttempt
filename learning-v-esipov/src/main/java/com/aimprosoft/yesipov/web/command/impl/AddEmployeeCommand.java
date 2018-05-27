package com.aimprosoft.yesipov.web.command.impl;

import com.aimprosoft.yesipov.web.Path;
import com.aimprosoft.yesipov.db.dao.impl.MySQLEmployeeDAO;
import com.aimprosoft.yesipov.db.entity.Department;
import com.aimprosoft.yesipov.db.entity.Employee;
import com.aimprosoft.yesipov.web.command.Command;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

public class AddEmployeeCommand implements Command {

    private static final Logger log = Logger.getLogger(AddEmployeeCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.debug("Command starts");

        String errorMessage = null;
        String forward = Path.ERROR_PAGE;

        String email = request.getParameter("email").trim();

        request.setAttribute("add_mail", email);

        List<Employee> employees = (List<Employee>) request.getServletContext().getAttribute("employeeList");

        Object object = employees.stream()
                .filter(x -> email.equals(x.getEmail()))
                .findAny()
                .orElse(null);

        if (object == null) {
            MySQLEmployeeDAO mySQLEmployeeDAO = new MySQLEmployeeDAO();

            Employee employee = setUpEmployee(request, employees);
            employee.setEmail(email);

            mySQLEmployeeDAO.addEmployee(employee);

            employees = mySQLEmployeeDAO.employeeList();

            log.trace("Employees size = " + employees.size());
            request.getServletContext().setAttribute("employeeList", employees);

            forward = Path.ADD_EDIT_EMPLOYEE;
        } else {
            errorMessage = "An employee with such email already exists";
            request.setAttribute("errorMessage", errorMessage);
            log.error("errorMessage --> " + errorMessage);
            return forward;
        }


        log.trace("Forward address --> " + forward);
        log.debug("Controller finished, now go to forward address --> " + forward);

        log.debug("Command finished");
        return forward;
    }

    private Employee setUpEmployee(HttpServletRequest request, List<Employee> employees) {
        Employee employee = new Employee();

        employee.setId(employees.size() + 1);

        employee.setFirstName(request.getParameter("firstName"));
        employee.setLastName(request.getParameter("lastName"));

        employee.setBirthday(Date.valueOf(request.getParameter("birthday")));
        employee.setPosition(request.getParameter("position"));

        Department department = new Department();
        department.setId(Integer.valueOf(request.getParameter("departmentId")));
        employee.setDepartment(department);

        employee.setSalary(Double.valueOf(request.getParameter("salary")));

        request.setAttribute("add_first_name", request.getParameter("firstName"));
        request.setAttribute("add_last_name", request.getParameter("lastName"));
        request.setAttribute("add_birth", request.getParameter("birthday"));
        request.setAttribute("add_job", request.getParameter("birthday"));
        request.setAttribute("add_department_id", request.getParameter("departmentId"));
        request.setAttribute("add_wage", request.getParameter("salary"));

        return employee;
    }
}
