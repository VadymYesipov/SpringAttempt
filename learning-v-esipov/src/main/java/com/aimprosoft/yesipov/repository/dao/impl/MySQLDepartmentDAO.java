package com.aimprosoft.yesipov.repository.dao.impl;

import com.aimprosoft.yesipov.repository.dao.DepartmentDAO;
import com.aimprosoft.yesipov.repository.entity.Department;
import com.aimprosoft.yesipov.repository.utils.HibernateSessionFactory;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@ComponentScan
public class MySQLDepartmentDAO implements DepartmentDAO {

    private static final Logger log = Logger.getLogger(MySQLDepartmentDAO.class);

    @Autowired
    private SessionFactory sessionFactory;

    private static MySQLDepartmentDAO mySQLDepartmentDAO;

    private MySQLDepartmentDAO() {
    }

    @Bean
    public static MySQLDepartmentDAO getMySQLDepartmentDAO() {
        if (mySQLDepartmentDAO == null) {
            mySQLDepartmentDAO = new MySQLDepartmentDAO();
        }
        return mySQLDepartmentDAO;
    }

    @Override
    public void addDepartment(Department department) {
        Session session = null;

        try {
            //session = HibernateSessionFactory.getSessionFactory().openSession();
            session = sessionFactory.openSession();

            session.beginTransaction();

            session.save(department);

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
    public void editDepartment(Department department, Integer id) {
        log.debug("Editing a department");

        Session session = null;

        try {
            session = HibernateSessionFactory.getSessionFactory().openSession();

            session.beginTransaction();

            Query query = session.createQuery("update Department set id = :newId, originalName = :name " +
                    "where id = :id");

            query.setParameter("newId", department.getId());
            query.setParameter("name", department.getOriginalName());
            query.setParameter("id", id);

            int result = query.executeUpdate();
            log.debug("Count of updated departments:" + result);

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
    public void removeDepartment(Department department) {
        Session session = null;

        try {
            session = HibernateSessionFactory.getSessionFactory().openSession();

            session.beginTransaction();

            Query query = session.createQuery("delete Department where id = :id");
            query.setParameter("id", department.getId());
            int result = query.executeUpdate();
            log.debug("Count of deleted elements: " + result);

            session.getTransaction().commit();

            session.close();
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
    public List<Department> departmentsList() {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();

        session.beginTransaction();

        Query query = session.createQuery("from Department order by id");
        List<Department> departments = query.list();
        departments.forEach(System.out::println);

        session.getTransaction().commit();

        session.close();

        return departments;
    }
}
