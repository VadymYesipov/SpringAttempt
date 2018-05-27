package com.aimprosoft.yesipov.repository.service;

import com.aimprosoft.yesipov.repository.dao.DepartmentDAO;
import com.aimprosoft.yesipov.repository.entity.Department;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@ComponentScan
public class MyServiceImpl {

    private static final Logger log = Logger.getLogger(MyServiceImpl.class);

    @Autowired
    private DepartmentDAO departmentDAO;

    @Transactional
    public List<Department> execute(Department department) {

        departmentDAO.addDepartment(department);

        List<Department> departments = departmentDAO.departmentsList();

        log.debug("Command finished");

        return departments;
    }
}
