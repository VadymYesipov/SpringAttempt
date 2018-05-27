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

public class FilteredListCommand implements Command {

    private static final Logger log = Logger.getLogger(FilteredListCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.debug("Command starts");

        String forward = Path.EMPLOYEES_JSP;

        Integer id = Integer.valueOf(request.getParameter("id"));

        List<Employee> employees = new MySQLEmployeeDAO().filteredEmployeeList(id);
        if (employees.size() > 0) {
            log.trace("Employee size = " + employees.size());
        }
        request.getServletContext().setAttribute("employeeList", employees);

        log.trace("Forward address --> " + forward);
        log.debug("Controller finished, now go to forward address --> " + forward);

        log.debug("Command finished");
        return forward;
    }
}
