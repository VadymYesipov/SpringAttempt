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

public class EditDepartmentCommand implements Command {

    private static final Logger log = Logger.getLogger(EditDepartmentCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.debug("Command starts");

        String errorMessage = null;
        String forward = Path.ERROR_PAGE;

        Integer id = Integer.valueOf(request.getParameter("id"));
        String name = request.getParameter("departmentName").trim();

        request.setAttribute("edit_ID", id);
        request.setAttribute("edit_name", name);

        List<Department> departments = (List<Department>) request.getServletContext().getAttribute("departmentList");

        Department object = departments.stream()
                .filter(x -> name.equals(x.getName()))
                .findAny()
                .orElse(null);

        if (object != null) {
            errorMessage = "A department with such name already exists";
            request.setAttribute("errorMessage", errorMessage);
            log.error("errorMessage --> " + errorMessage);
        } else {
            MySQLDepartmentDAO mySQLDepartmentDAO = new MySQLDepartmentDAO();

            Department department = new Department();
            department.setId(id);
            department.setName(name);

            mySQLDepartmentDAO.editDepartment(department);

            departments = mySQLDepartmentDAO.departmentsList();

            log.trace("Departments size = " + departments.size());
            request.getServletContext().setAttribute("departmentList", departments);

            forward = Path.ADD_EDIT_DEPARTMENT;
        }


        log.trace("Forward address --> " + forward);
        log.debug("Controller finished, now go to forward address --> " + forward);

        log.debug("Command finished");
        return forward;
    }


}
