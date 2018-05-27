package com.aimprosoft.yesipov.web.filter;

import com.aimprosoft.yesipov.db.entity.Department;
import com.aimprosoft.yesipov.db.entity.Employee;
import com.aimprosoft.yesipov.repository.dao.DepartmentDAO;
import com.aimprosoft.yesipov.repository.dao.EmployeeDAO;
import com.aimprosoft.yesipov.repository.dao.impl.MySQLDepartmentDAO;
import com.aimprosoft.yesipov.repository.dao.impl.MySQLEmployeeDAO;
import com.aimprosoft.yesipov.web.service.ListService;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public class EncodingFilter implements Filter {

    private static final Logger log = Logger.getLogger(EncodingFilter.class);

    private String encoding;

    public void destroy() {
        log.debug("Filter destruction starts");
        // do nothing
        log.debug("Filter destruction finished");
    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        log.debug("Filter starts");

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        log.trace("Request uri --> " + httpRequest.getRequestURI());

        String requestEncoding = request.getCharacterEncoding();
        if (requestEncoding == null) {
            log.trace("Request encoding = null, set encoding --> " + encoding);
            request.setCharacterEncoding(encoding);
        }

        setLists(request);

        log.debug("Filter finished");
        chain.doFilter(request, response);
    }

    private void setLists(ServletRequest request) {
        DepartmentDAO departmentDAO = MySQLDepartmentDAO.getMySQLDepartmentDAO();
        EmployeeDAO employeeDAO = MySQLEmployeeDAO.getMySQLEmployeeDAO();

        ListService listService = new ListService(departmentDAO, employeeDAO);
        request.setAttribute("employeeList", listService.getEmployeeList());
        request.setAttribute("departmentList", listService.getDepartmentList());

        /*List<Department> departments = new MySQLDepartmentDAO().departmentsList();
        if (departments.size() > 0) {
            log.trace("Departments size = " + departments.size());
            request.getServletContext().setAttribute("departmentList", departments);
        }

        List<Employee> employees = new MySQLEmployeeDAO().employeeList();
        if (employees.size() > 0) {
            log.trace("Employee size = " + employees.size());
            request.getServletContext().setAttribute("employeeList", employees);
        }*/
    }

    public void init(FilterConfig fConfig) throws ServletException {
        log.debug("Filter initialization starts");
        encoding = fConfig.getInitParameter("encoding");
        log.trace("Encoding from web.xml --> " + encoding);
        log.debug("Filter initialization finished");
    }

}
