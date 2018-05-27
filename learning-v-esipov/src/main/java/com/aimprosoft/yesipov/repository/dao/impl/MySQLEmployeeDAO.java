package com.aimprosoft.yesipov.repository.dao.impl;

import com.aimprosoft.yesipov.repository.dao.EmployeeDAO;
import com.aimprosoft.yesipov.repository.entity.Employee;
import com.aimprosoft.yesipov.repository.utils.HibernateSessionFactory;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class MySQLEmployeeDAO implements EmployeeDAO {

    private static final Logger log = Logger.getLogger(MySQLEmployeeDAO.class);

    private static MySQLEmployeeDAO mySQLEmployeeDAO;

    private MySQLEmployeeDAO() {
    }

    public static MySQLEmployeeDAO getMySQLEmployeeDAO() {
        if (mySQLEmployeeDAO == null) {
            mySQLEmployeeDAO = new MySQLEmployeeDAO();
        }
        return mySQLEmployeeDAO;
    }

    @Override
    public void addEmployee(Employee employee) {
        Session session = null;

        try {
            session = HibernateSessionFactory.getSessionFactory().openSession();

            session.beginTransaction();

            session.save(employee);

            session.getTransaction().commit();
        } catch (RuntimeException e) {
            try {
                session.getTransaction().rollback();
            } catch (RuntimeException e1) {
                log.error("Couldn't roll back transaction", e1);
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void editEmployee(Employee employee, Integer id) {
        log.debug("Editing an employee");

        Session session = null;

        try {
            session = HibernateSessionFactory.getSessionFactory().openSession();

            session.beginTransaction();

            Query query = session.createQuery("update Employee " +
                    "set id = :newId, firstName = :firstName, lastName = :lastName, birthday = :birthday, email = :email, " +
                    "job = :job, departmentByDepartmentId.id = :department_id, salary = :salary " +
                    "where id = :id");

            query.setParameter("newId", employee.getId());
            query.setParameter("firstName", employee.getFirstName());
            query.setParameter("lastName", employee.getLastName());
            query.setParameter("birthday", employee.getBirthday());
            query.setParameter("email", employee.getEmail());
            query.setParameter("job", employee.getJob());
            query.setParameter("department_id", employee.getDepartmentByDepartmentId().getId());
            query.setParameter("salary", employee.getSalary());
            query.setParameter("id", id);

            int result = query.executeUpdate();
            log.debug("Count of updated employees:" + result);
            //session.saveOrUpdate(employee);

            session.getTransaction().commit();
        } catch (RuntimeException e) {
            try {
                session.getTransaction().rollback();
            } catch (RuntimeException e1) {
                log.error("Couldn't roll back transaction", e1);
            }
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void removeEmployee(Employee employee) {
        Session session = null;

        try {
            session = HibernateSessionFactory.getSessionFactory().openSession();

            session.beginTransaction();

            Query query =  session.createQuery("delete Employee where id = :id");
            query.setParameter("id", employee.getId());
            int result = query.executeUpdate();
            log.debug("Count of deleted elements: " + result);

            session.getTransaction().commit();
        } catch (RuntimeException e) {
            try {
                session.getTransaction().rollback();
            } catch (RuntimeException e1) {
                log.error("Couldn't roll back transaction", e1);
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Employee> employeeList() {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();

        session.beginTransaction();

        Query query = session.createQuery("from Employee ");
        List<Employee> employees = query.list();
        employees.forEach(System.out::println);

        session.getTransaction().commit();

        session.close();

        return employees;
    }

    @Override
    public List<Employee> filteredEmployeeList(Integer id) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();

        session.beginTransaction();

        Query query = session.createQuery("from Employee where departmentByDepartmentId.id = :id");
        query.setParameter("id", id);

        List<Employee> employees = query.list();
        employees.forEach(System.out::println);

        session.getTransaction().commit();

        session.close();

        return employees;
    }
}
