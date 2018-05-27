package com.aimprosoft.yesipov.web.command.impl;

import com.aimprosoft.yesipov.web.Path;
import com.aimprosoft.yesipov.db.dao.impl.MySQLDepartmentDAO;
import com.aimprosoft.yesipov.db.entity.Department;
import com.aimprosoft.yesipov.web.command.Command;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class AddDepartmentCommand implements Command {

    private static final Logger log = Logger.getLogger(AddDepartmentCommand.class);

    MySQLDepartmentDAO mySQLDepartmentDAO;
    Department department;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.debug("Command starts");

        String errorMessage = null;
        String forward = Path.ERROR_PAGE;

        String name = request.getParameter("departmentName").trim();

        List<Department> departments = (List<Department>) request.getServletContext().getAttribute("departmentList");

        Object object = departments.stream()
                .filter(x -> name.equals(x.getName()))
                .findAny()
                .orElse(null);

        if (object == null) {

            mySQLDepartmentDAO = new MySQLDepartmentDAO();

            department.setId(departments.size() + 1);
            department.setName(name);

            mySQLDepartmentDAO.addDepartment(department);

            departments = mySQLDepartmentDAO.departmentsList();

            log.trace("Departments size = " + departments.size());
            request.getServletContext().setAttribute("departmentList", departments);

            forward = Path.ADD_EDIT_DEPARTMENT;
        } else {
            errorMessage = "A department with such name already exists";
            request.setAttribute("errorMessage", errorMessage);
            log.error("errorMessage --> " + errorMessage);
            return forward;
        }

        request.setAttribute("add_name", name);

        log.trace("Forward address --> " + forward);
        log.debug("Controller finished, now go to forward address --> " + forward);

        log.debug("Command finished");
        return forward;
    }
}
