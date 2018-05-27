INSERT INTO department (original_name) VALUES ("department");

UPDATE department
SET original_name = "pisun"
WHERE id=17;

DELETE FROM department WHERE id=17;



SELECT * FROM example.department;