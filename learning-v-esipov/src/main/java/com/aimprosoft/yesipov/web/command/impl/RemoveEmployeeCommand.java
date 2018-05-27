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
import java.util.List;

public class RemoveEmployeeCommand implements Command {

    private static final Logger log = Logger.getLogger(RemoveEmployeeCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.debug("Command starts");

        String errorMessage = null;
        String forward = Path.ERROR_PAGE;

        String email = request.getParameter("email").trim();

        List<Employee> employees = (List<Employee>) request.getServletContext().getAttribute("employeeList");

        Employee employee = employees.stream()
                .filter(x -> email.equals(x.getEmail()))
                .findAny()
                .orElse(null);

        if (employee == null) {
            errorMessage = "An employee with such email doesn't exist";
            request.setAttribute("errorMessage", errorMessage);
            log.error("errorMessage --> " + errorMessage);
            return forward;
        } else {
            MySQLEmployeeDAO mySQLEmployeeDAO = new MySQLEmployeeDAO();

            mySQLEmployeeDAO.removeEmployee(employee, employees.size());

            System.out.println(employee.getDepartment().getId());

            employees = new MySQLEmployeeDAO().filteredEmployeeList(employee.getDepartment().getId());

            log.trace("Employee size = " + employees.size());
            request.getServletContext().setAttribute("employeeList", employees);

            forward = Path.EMPLOYEES_JSP;
        }


        log.trace("Forward address --> " + forward);
        log.debug("Controller finished, now go to forward address --> " + forward);

        log.debug("Command finished");
        return forward;
    }
}
