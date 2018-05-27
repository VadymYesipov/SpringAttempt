package com.aimprosoft.yesipov.web;

import com.aimprosoft.yesipov.repository.dao.DepartmentDAO;
import com.aimprosoft.yesipov.repository.dao.EmployeeDAO;
import com.aimprosoft.yesipov.repository.dao.impl.MySQLDepartmentDAO;
import com.aimprosoft.yesipov.repository.dao.impl.MySQLEmployeeDAO;
import com.aimprosoft.yesipov.repository.entity.Department;
import com.aimprosoft.yesipov.repository.entity.Employee;
import com.aimprosoft.yesipov.web.command.Command;
import com.aimprosoft.yesipov.web.command.CommandContainer;
import com.aimprosoft.yesipov.web.service.AddEntityService;
import com.aimprosoft.yesipov.web.service.EditEntityService;
import com.aimprosoft.yesipov.web.service.ListService;
import com.aimprosoft.yesipov.web.service.RemoveEntityService;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.stream.Stream;

public class Controller extends HttpServlet {

    private static final long serialVersionUID = 2423353715955164816L;

    private static final Logger log = Logger.getLogger(Controller.class);

    private DepartmentDAO departmentDAO = MySQLDepartmentDAO.getMySQLDepartmentDAO();
    private EmployeeDAO employeeDAO = MySQLEmployeeDAO.getMySQLEmployeeDAO();

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    /**
     * Main method of this controller.
     */
    private void process(HttpServletRequest request,
                         HttpServletResponse response) throws IOException, ServletException {
        log.debug("Controller starts");

        // extract command name from the request
        String commandName = request.getParameter("command");
        log.trace("Request parameter: command --> " + commandName);

        // obtain command object by its name
        //Command command = CommandContainer.get(commandName);
        //log.trace("Obtained command --> " + command);

        // execute command and get forward address
        //String forward = command.execute(request, response);
        String forward = executeService(commandName, request);
        log.trace("Forward address --> " + forward);

        log.debug("Controller finished, now go to forward address --> " + forward);

        // if the forward address is not null go to the address
        if (forward != null) {
            RequestDispatcher disp = request.getRequestDispatcher(forward);
            disp.forward(request, response);
        }
    }

    private String executeService(String name, HttpServletRequest request) {
        switch (name) {
            case "list":
                return request.getParameter("name").equals("departments") ?
                        Path.DEPARTMENTS_JSP :
                        Path.ALL_EMPLOYEES_JSP;
            case "filteredList":
                return executeFilteredListService(request);
            case "addEdit":
                return request.getParameter("name").equals("departments") ?
                        Path.ADD_EDIT_DEPARTMENT :
                        Path.ADD_EDIT_EMPLOYEE;
            case "removeEmployee":
                return removeEmployeeAndReturnPath(request);
            case "removeDepartment":
                return removeDepartmentAndReturnPath(request);
            case "addEmployee":
                return addEmployeeAndReturnPath(request);
            case "addDepartment":
                return addDepartmentAndReturnPath(request);
            case "editEmployee":
                return editEmployeeAndReturnPath(request);
            case "editDepartment":
                return editDepartmentAndReturnPath(request);
        }
        return null;
    }

    private String executeFilteredListService(HttpServletRequest request) {

        String errorMessage = null;
        String forward = Path.DEPARTMENTS_JSP;

        Integer id = Integer.valueOf(request.getParameter("id"));
        request.setAttribute("ID", id);

        List<Department> departments = (List<Department>) request.getAttribute("departmentList");

        Department department = departments.stream()
                .filter(x -> id.equals(x.getId()))
                .findAny()
                .orElse(null);

        if (department != null) {
            ListService listService = new ListService(departmentDAO, employeeDAO);

            request.setAttribute("employeeList",
                    listService.getFilteredList(id));

            forward = Path.EMPLOYEES_JSP;
        } else {
            errorMessage = "A department with such id doesn't exist";
            request.setAttribute("errorMessage", errorMessage);
            log.error("errorMessage --> " + errorMessage);
        }

        log.trace("Forward address --> " + forward);
        log.debug("Controller finished, now go to forward address --> " + forward);

        log.debug("Command finished");
        return forward;
    }

    private String editEmployeeAndReturnPath(HttpServletRequest request) {
        log.debug("Command starts");

        String errorMessage = null;
        String forward = Path.ADD_EDIT_EMPLOYEE;

        Integer id = Integer.valueOf(request.getParameter("id"));

        String newID = request.getParameter("newId");
        Integer newId = newID.equals("") ? 0 : Integer.valueOf(newID);

        String email = request.getParameter("email").trim();

        List<Employee> employees = (List<Employee>) request.getAttribute("employeeList");

        Employee object1 = employees.stream()
                .filter(x -> id.equals(x.getId()))
                .findAny()
                .orElse(null);

        Employee object2 = employees.stream()
                .filter(x -> x.getEmail().equals(email) || newId.equals(x.getId()))
                .findAny()
                .orElse(null);

        String departmentID = request.getParameter("departmentId");
        Integer departmentId = departmentID.equals("") ? 0 : Integer.valueOf(departmentID);

        List<Department> departments = (List<Department>) request.getAttribute("departmentList");

        Object object3 = departments.stream()
                .filter(x -> departmentId.equals(x.getId()))
                .findAny()
                .orElse(null);

        System.out.println((object2 != null) + "   " + (object1 == null) + "   " + (object3 == null));

        if (object2 != null || object1 == null || object3 == null) {
            errorMessage = "An employee with such email or id already exists or department id is incorrect";
            request.setAttribute("errorMessage", errorMessage);
            log.error("errorMessage --> " + errorMessage);
        } else {

            Employee employee = setUpEmployee(request, employees, id, newId);

            employees = new EditEntityService(employeeDAO).execute(employee, id);

            log.trace("Employees size = " + employees.size());
            request.setAttribute("employeeList", employees);
        }

        setUpValuesAfterEditing(request);

        log.trace("Forward address --> " + forward);
        log.debug("Controller finished, now go to forward address --> " + forward);
        log.debug("Command finished");

        return forward;
    }

    private Employee setUpEmployee(HttpServletRequest request, List<Employee> employees, Integer id, Integer newId) {

        Employee employee = new Employee();

        employee.setId(newId.equals(0) ? id : newId);

        String firstName = request.getParameter("firstName");
        firstName = firstName.equals("")
                ? getStream(employees, id)
                .map(Employee::getFirstName)
                .findAny()
                .orElse("")
                : firstName;
        employee.setFirstName(firstName);

        String lastName = request.getParameter("lastName");
        lastName = lastName.equals("")
                ? getStream(employees, id)
                .map(Employee::getLastName)
                .findAny()
                .orElse("")
                : lastName;
        employee.setLastName(lastName);

        String birth = request.getParameter("birthday");
        java.util.Date birthday = birth.equals("")
                ? getStream(employees, id)
                .map(Employee::getBirthday)
                .findAny()
                .orElse(null)
                : Date.valueOf(request.getParameter("birthday"));
        employee.setBirthday(birthday);

        String email = request.getParameter("email");
        email = email.equals("")
                ? getStream(employees, id)
                .map(Employee::getEmail)
                .findAny()
                .orElse(null)
                : email;
        employee.setEmail(email);

        String job = request.getParameter("position");
        job = job.equals("")
                ? getStream(employees, id)
                .map(Employee::getJob)
                .findAny()
                .orElse("")
                : job;
        employee.setJob(job);

        String departmentID = request.getParameter("departmentId");
        Integer departmentId = departmentID.equals("") ?
                getStream(employees, id)
                        .map(Employee::getDepartmentByDepartmentId)
                        .findAny()
                        .orElse(null)
                        .getId()
                : Integer.valueOf(departmentID);
        Department department = new Department();
        department.setId(departmentId);
        employee.setDepartmentByDepartmentId(department);

        String wage = request.getParameter("salary");
        Double salary = wage.equals("") ?
                getStream(employees, id)
                        .map(Employee::getSalary)
                        .findAny()
                        .orElse(null)
                : Double.valueOf(wage);
        employee.setSalary(salary);

        return employee;
    }

    private Stream<Employee> getStream(List<Employee> employees, Integer id) {
        return employees.stream()
                .filter(x -> id.equals(x.getId()));
    }

    private String editDepartmentAndReturnPath(HttpServletRequest request) {
        log.debug("Command starts");

        String errorMessage = null;
        String forward = Path.ADD_EDIT_DEPARTMENT;

        Integer id = Integer.valueOf(request.getParameter("id"));

        String newID = request.getParameter("newId");
        Integer newId = newID.equals("") ? -1 : Integer.valueOf(newID);

        String name = request.getParameter("departmentName").trim();

        request.setAttribute("edit_ID", id);
        request.setAttribute("new_edit_ID", newId);
        request.setAttribute("edit_name", name);

        List<Department> departments = (List<Department>) request.getAttribute("departmentList");

        Department object1 = departments.stream()
                .filter(x -> id.equals(x.getId()))
                .findAny()
                .orElse(null);

        Department object2 = departments.stream()
                .filter(x -> x.getOriginalName().equals(name) || newId.equals(x.getId()))
                .findAny()
                .orElse(null);

        if (object2 != null || object1 == null) {
            errorMessage = "A department with such name or id already exists";
            request.setAttribute("errorMessage", errorMessage);
            log.error("errorMessage --> " + errorMessage);
        } else {

            Department department = new Department();

            department.setId(newId == -1 ? id : newId);
            String originalName = name.equals("")
                    ? departments.stream()
                    .filter(x -> id.equals(x.getId()))
                    .map(Department::getOriginalName)
                    .findAny()
                    .orElse("")
                    : name;
            department.setOriginalName(originalName);

            departments = new EditEntityService(departmentDAO).execute(department, id);

            log.trace("Departments size = " + departments.size());
            request.setAttribute("departmentList", departments);
        }

        log.trace("Forward address --> " + forward);
        log.debug("Controller finished, now go to forward address --> " + forward);

        log.debug("Command finished");
        return forward;
    }

    private String removeEmployeeAndReturnPath(HttpServletRequest request) {
        String errorMessage = null;
        String forward = Path.EMPLOYEES_JSP;
        String email = request.getParameter("email").trim();

        List<Employee> employees = (List<Employee>) request.getAttribute("employeeList");

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
            employees = new RemoveEntityService(employeeDAO).execute(employee);

            log.trace("Employee size = " + employees.size());
            request.setAttribute("employeeList", employees);
        }

        return forward;
    }

    private String removeDepartmentAndReturnPath(HttpServletRequest request) {
        log.debug("Command starts");

        String errorMessage = null;
        String forward = Path.DEPARTMENTS_JSP;

        Integer id = Integer.valueOf(request.getParameter("id"));

        request.setAttribute("remove_ID", id);

        List<Department> departments = (List<Department>) request.getAttribute("departmentList");
        List<Employee> employees = (List<Employee>) request.getAttribute("employeeList");

        Department department = departments.stream()
                .filter(x -> id.equals(x.getId()))
                .findAny()
                .orElse(null);

        if (department == null) {
            errorMessage = "A department with such id doesn't exist";
            request.setAttribute("errorMessage", errorMessage);
            log.error("errorMessage --> " + errorMessage);
            return forward;
        } else {

            departments = new RemoveEntityService(departmentDAO).execute(department);

            log.trace("Departments size = " + departments.size());
            log.trace("Employees size = " + employees.size());

            request.setAttribute("departmentList", departments);
            request.setAttribute("employeeList", employees);
        }


        log.trace("Forward address --> " + forward);
        log.debug("Controller finished, now go to forward address --> " + forward);

        log.debug("Command finished");
        return forward;
    }

    private String addEmployeeAndReturnPath(HttpServletRequest request) {
        log.debug("Command starts");

        String errorMessage = null;
        String forward = Path.ADD_EDIT_EMPLOYEE;

        String email = request.getParameter("email").trim();

        request.setAttribute("add_mail", email);

        setUpValuesAfterAdding(request);

        List<Employee> employees = (List<Employee>) request.getAttribute("employeeList");

        Object object1 = employees.stream()
                .filter(x -> email.equals(x.getEmail()))
                .findAny()
                .orElse(null);

        Integer departmentId = Integer.valueOf(request.getParameter("departmentId"));

        List<Department> departments = (List<Department>) request.getAttribute("departmentList");

        Object object2 = departments.stream()
                .filter(x -> departmentId.equals(x.getId()))
                .findAny()
                .orElse(null);

        if (object1 == null && object2 != null) {

            Employee employee = setUpEmployee(request);
            employee.setEmail(email);

            employees = new AddEntityService(employeeDAO).execute(employee);

            log.trace("Employees size = " + employees.size());
            request.setAttribute("employeeList", employees);
        } else {
            errorMessage = "An employee with such email already exists or department id is incorrect";
            request.setAttribute("errorMessage", errorMessage);
            log.error("errorMessage --> " + errorMessage);
            return forward;
        }


        log.trace("Forward address --> " + forward);
        log.debug("Controller finished, now go to forward address --> " + forward);

        log.debug("Command finished");
        return forward;
    }

    private Employee setUpEmployee(HttpServletRequest request) {
        Employee employee = new Employee();

        employee.setFirstName(request.getParameter("firstName"));
        employee.setLastName(request.getParameter("lastName"));

        employee.setBirthday(Date.valueOf(request.getParameter("birthday")));
        employee.setJob(request.getParameter("position"));

        Department department = new Department();
        department.setId(Integer.valueOf(request.getParameter("departmentId")));
        employee.setDepartmentByDepartmentId(department);

        employee.setSalary(Double.valueOf(request.getParameter("salary")));

        return employee;
    }

    private String addDepartmentAndReturnPath(HttpServletRequest request) {
        log.debug("Command starts");

        String errorMessage = null;
        String forward = Path.ADD_EDIT_DEPARTMENT;

        String name = request.getParameter("departmentName").trim();

        request.setAttribute("add_name", name);

        List<Department> departments = (List<Department>) request.getAttribute("departmentList");

        Object object = departments.stream()
                .filter(x -> name.equals(x.getOriginalName()))
                .findAny()
                .orElse(null);

        if (object == null) {

            Department department = new Department();
            //department.setId(departments.size() + 1);
            department.setOriginalName(name);

            departments = new AddEntityService(departmentDAO).execute(department);

            log.trace("Departments size = " + departments.size());
            request.setAttribute("departmentList", departments);
        } else {
            errorMessage = "A department with such name already exists";
            request.setAttribute("errorMessage", errorMessage);
            log.error("errorMessage --> " + errorMessage);
            return forward;
        }

        log.trace("Forward address --> " + forward);
        log.debug("Controller finished, now go to forward address --> " + forward);

        log.debug("Command finished");
        return forward;
    }

    private void setUpValuesAfterEditing(HttpServletRequest request) {


        request.setAttribute("edit_ID", request.getParameter("id"));
        request.setAttribute("new_edit_ID", request.getParameter("newId"));
        request.setAttribute("edit_mail", request.getParameter("email"));
        request.setAttribute("edit_first_name", request.getParameter("firstName"));
        request.setAttribute("edit_last_name", request.getParameter("lastName"));
        request.setAttribute("edit_birth", request.getParameter("birthday"));
        request.setAttribute("edit_job", request.getParameter("position"));
        request.setAttribute("edit_department_id", request.getParameter("departmentId"));
        request.setAttribute("edit_wage", request.getParameter("salary"));
    }

    private void setUpValuesAfterAdding(HttpServletRequest request) {
        request.setAttribute("add_first_name", request.getParameter("firstName"));
        request.setAttribute("add_last_name", request.getParameter("lastName"));
        request.setAttribute("add_birth", request.getParameter("birthday"));
        request.setAttribute("add_job", request.getParameter("position"));
        request.setAttribute("add_department_id", request.getParameter("departmentId"));
        request.setAttribute("add_wage", request.getParameter("salary"));
    }
}