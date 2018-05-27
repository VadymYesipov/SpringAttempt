import com.aimprosoft.yesipov.repository.entity.Department;
import com.aimprosoft.yesipov.repository.service.MyService;
import com.aimprosoft.yesipov.repository.service.MyServiceImpl;
import org.hibernate.HibernateException;
import org.hibernate.Metamodel;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.metamodel.EntityType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@EnableAutoConfiguration
public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    @Autowired
    MyService myService;

    public static void main(String args[]) {
        SpringApplication.run(Main.class, args);
        Department department = new Department();
        department.setOriginalName("hui");
        System.out.println(myService.execute(department));
    }

    @Autowired
    JdbcTemplate jdbcTemplate;
}