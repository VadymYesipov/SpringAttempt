SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP DATABASE IF EXISTS example;
CREATE DATABASE IF NOT EXISTS example DEFAULT CHARACTER SET utf8;
USE example;

DROP TABLE IF EXISTS `example`.`department`;
CREATE TABLE IF NOT EXISTS department (
id INT NOT NULL AUTO_INCREMENT,
original_name VARCHAR(255) NOT NULL UNIQUE,
PRIMARY KEY (id)
);

DROP TABLE IF EXISTS `example`.`employee`;
CREATE TABLE IF NOT EXISTS employee (
id INT NOT NULL AUTO_INCREMENT UNIQUE,
first_name VARCHAR(255) NOT NULL,
last_name VARCHAR(255) NOT NULL,
birthday DATE NOT NULL,
email VARCHAR(255) NOT NULL UNIQUE,
job VARCHAR(255) NOT NULL,
department_id INT NOT NULL,
salary DOUBLE NOT NULL,
PRIMARY KEY (id),
INDEX `department_id_idx` (`department_id` ASC),
CONSTRAINT `department_id`
FOREIGN KEY (`department_id`)
REFERENCES `example`.`department` (`id`)
ON DELETE CASCADE
ON UPDATE CASCADE
);

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

INSERT INTO department (id, original_name) VALUES (1, "Accounting department");
INSERT INTO department (id, original_name) VALUES (2, "Purchasing department");
INSERT INTO department (id, original_name) VALUES (3, "Management");
INSERT INTO department (id, original_name) VALUES (4, "Administrative department");
INSERT INTO department (id, original_name) VALUES (5, "Logistics");
INSERT INTO department (id, original_name) VALUES (6, "IT department");
INSERT INTO department (id, original_name) VALUES (7, "Marketing");
INSERT INTO department (id, original_name) VALUES (8, "Public relations department");
INSERT INTO department (id, original_name) VALUES (9, "Sales department");
INSERT INTO department (id, original_name) VALUES (10, "Production department");
INSERT INTO department (id, original_name) VALUES (11, "Shipping department");
INSERT INTO department (id, original_name) VALUES (12, "Import department");
INSERT INTO department (id, original_name) VALUES (13, "Export department");
INSERT INTO department (id, original_name) VALUES (14, "Engineering department");
INSERT INTO department (id, original_name) VALUES (15, "Human resources");

INSERT INTO employee (id, first_name, last_name, birthday, email, job, department_id, salary) VALUES (1, "Conor", "McGregor", "1962-03-18", "c.mcgregor@gmail.com", "Analyst", 5, 9937.52);
INSERT INTO employee (id, first_name, last_name, birthday, email, job, department_id, salary) VALUES (2, "Max", "Holloway", "1965-11-06", "m.holloway@gmail.com", "Director of boards", 4, 4588.36);
INSERT INTO employee (id, first_name, last_name, birthday, email, job, department_id, salary) VALUES (3, "Demetrius", "Johnson", "1966-05-04", "d.johnson@gmail.com", "CEO", 4, 6712.43);
INSERT INTO employee (id, first_name, last_name, birthday, email, job, department_id, salary) VALUES (4, "Amanda", "Nunes", "1968-10-15", "a.nunes@gmail.com", "Purchasing manager", 2, 5507.53);
INSERT INTO employee (id, first_name, last_name, birthday, email, job, department_id, salary) VALUES (5, "Michelle", "Waterson", "1970-12-01", "m.waterson@gmail.com", "CFO", 4, 6618.11);
INSERT INTO employee (id, first_name, last_name, birthday, email, job, department_id, salary) VALUES (6, "Cody", "Garbrandt", "1972-05-13", "c.garbrandt@gmail.com", "Managing director", 3, 5167.12);
INSERT INTO employee (id, first_name, last_name, birthday, email, job, department_id, salary) VALUES (7, "Michael", "Bisping", "1976-10-30", "m.bisping@gmail.com", "Commercial agent", 10, 3453.33);
INSERT INTO employee (id, first_name, last_name, birthday, email, job, department_id, salary) VALUES (8, "Holly", "Holm", "1980-01-29", "h.holm@gmail.com", "Service engineer", 14, 7157.40);
INSERT INTO employee (id, first_name, last_name, birthday, email, job, department_id, salary) VALUES (9, "Stipe", "Miocic", "1986-07-29", "s.miocic@gmail.com", "Senior executive", 4, 9353.89);
INSERT INTO employee (id, first_name, last_name, birthday, email, job, department_id, salary) VALUES (10, "Travis", "Brown", "1988-05-16", "t.brown@gmail.com", "Sales representative", 9, 2226.49);
INSERT INTO employee (id, first_name, last_name, birthday, email, job, department_id, salary) VALUES (11, "Robert", "Whittaker", "1989-01-19", "r.whittaker@gmail.com", "Sales manager", 9, 4943.68);
INSERT INTO employee (id, first_name, last_name, birthday, email, job, department_id, salary) VALUES (12, "Mark", "Hunt", "1990-03-08", "m.hunt@gmail.com", "Export manager", 13, 1990.45);
INSERT INTO employee (id, first_name, last_name, birthday, email, job, department_id, salary) VALUES (13, "Ronda", "Rousey", "1997-11-18", "r.rousey@gmail.com", "Import manager", 12, 1168.11);
INSERT INTO employee (id, first_name, last_name, birthday, email, job, department_id, salary) VALUES (14, "Misha", "Tate", "1997-12-15", "m.tate@gmail.com", "Accountant", 1, 9510.84);
INSERT INTO employee (id, first_name, last_name, birthday, email, job, department_id, salary) VALUES (15, "Dustin", "Poirier", "1998-03-06", "d.poirier@gmail.com", "PR manager", 8, 9402.46);