package com.aimprosoft.yesipov.web.command.impl;

import com.aimprosoft.yesipov.web.Path;
import com.aimprosoft.yesipov.db.dao.impl.MySQLEmployeeDAO;
import com.aimprosoft.yesipov.db.entity.Employee;
import com.aimprosoft.yesipov.web.command.Command;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EditEmployeeCommand implements Command {

    private static final Logger log = Logger.getLogger(EditEmployeeCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.debug("Command starts");

        String errorMessage = null;
        String forward = Path.ERROR_PAGE;

        Integer id = Integer.valueOf(request.getParameter("id"));
        String email = request.getParameter("email").trim();

        request.setAttribute("ID", id);
        request.setAttribute("mail", email);

        List<Employee> employees = (List<Employee>) request.getServletContext().getAttribute("employeeList");

        Employee object = employees.stream()
                .filter(x -> email.equals(x.getEmail()))
                .findAny()
                .orElse(null);

        if (object != null) {
            errorMessage = "An employee with such email already exists";
            request.setAttribute("errorMessage", errorMessage);
            log.error("errorMessage --> " + errorMessage);
        } else {
            MySQLEmployeeDAO mySQLEmployeeDAO = new MySQLEmployeeDAO();

            String query = setUpQuery(request, email);

            Employee employee = new Employee();
            employee.setId(id);

            mySQLEmployeeDAO.editEmployee(employee, query);

            employees = mySQLEmployeeDAO.employeeList();

            log.trace("Employees size = " + employees.size());
            request.getServletContext().setAttribute("employeeList", employees);

            forward = Path.ADD_EDIT_EMPLOYEE;
        }

        log.trace("Forward address --> " + forward);
        log.debug("Controller finished, now go to forward address --> " + forward);
        log.debug("Command finished");

        return forward;
    }

    private String setUpQuery(HttpServletRequest request, String email) {

        Map<String, String> parameters = new LinkedHashMap<>();

        parameters.put(request.getParameter("firstName"), "first_name");
        parameters.put(request.getParameter("lastName"), "last_name");
        parameters.put(request.getParameter("birthday"), "birthday");
        parameters.put(email, "email");
        parameters.put(request.getParameter("position"), "job");
        parameters.put(request.getParameter("departmentId"), "department_id");
        parameters.put(request.getParameter("salary"), "salary");

        request.setAttribute("edit_first_name", request.getParameter("firstName"));
        request.setAttribute("edit_last_name", request.getParameter("lastName"));
        request.setAttribute("edit_birth", request.getParameter("birthday"));
        request.setAttribute("edit_job", request.getParameter("birthday"));
        request.setAttribute("edit_department_id", request.getParameter("departmentId"));
        request.setAttribute("edit_wage", request.getParameter("salary"));

        parameters.remove("");

        String query = parameters.entrySet().stream()
                .map(entry ->  entry.getValue() + "=" + entry.getKey())
                .collect(Collectors.toList())
                .toString();

        log.debug(parameters);
        log.debug(query.substring(1, query.length() - 1));

        return query.substring(1, query.length() - 1);
    }
}
