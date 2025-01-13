-- DROP TABLE IF EXISTS role;

CREATE TABLE role(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

INSERT INTO role (name) VALUES ('ROLE_ADMIN');
INSERT INTO role (name) VALUES ('ROLE_MANAGER');
INSERT INTO role (name) VALUES ('ROLE_EMPLOYEE');

CREATE TABLE departments (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(30) NOT NULL
);

INSERT INTO departments (name) VALUES ('IT');
INSERT INTO departments (name) VALUES ('SALES');
INSERT INTO departments (name) VALUES ('DEVELOPMENT');
INSERT INTO departments (name) VALUES ('NETWORKING');
INSERT INTO departments (name) VALUES ('HR');

CREATE TABLE request_type (
     id BIGSERIAL PRIMARY KEY,
     name VARCHAR(50) NOT NULL
);

INSERT INTO request_type (name) VALUES ('SICK_LEAVE');
INSERT INTO request_type (name) VALUES ('HALF_DATE');
INSERT INTO request_type (name) VALUES ('VACATION');
INSERT INTO request_type (name) VALUES ('WORK_FROM_HOME');

CREATE TABLE request_state (
    id SERIAL PRIMARY KEY,
    name VARCHAR(30) NOT NULL
);

INSERT INTO request_state (name) VALUES ('PENDING');
INSERT INTO request_state (name) VALUES ('APPROVED');
INSERT INTO request_state (name) VALUES ('REJECTED');

DROP TABLE IF EXISTS uuser;

CREATE TABLE uuser(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    password VARCHAR(255) DEFAULT NULL,
    username VARCHAR(30) DEFAULT NULL,
    role VARCHAR(50) DEFAULT NULL,
    CONSTRAINT UQ_uuser_username UNIQUE (username)
);
DROP TABLE IF EXISTS users_roles;

CREATE TABLE users_roles(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES uuser (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (role_id) REFERENCES role (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT UQ_users_roles_user_id UNIQUE (user_id) --kl user one role at a time
);

DROP TABLE IF EXISTS employees;

CREATE TABLE employees (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(100),
    user_id BIGINT UNIQUE, -- Foreign key to users table
    department_id INT,                   -- Foreign key referencing department
    FOREIGN KEY (department_id) REFERENCES departments(id), -- The foreign key constraint
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES uuser (id)
);

CREATE TABLE request (
     id SERIAL PRIMARY KEY,
     description VARCHAR(255),
     employee_id INT,
     status VARCHAR(25) DEFAULT NULL,
     request_type VARCHAR(25) DEFAULT NULL,
     request_type_id INT NOT NULL,
     state INT NOT NULL,
     request_date DATE DEFAULT CURRENT_DATE,
     FOREIGN KEY (request_type_id) REFERENCES request_type(id),
     FOREIGN KEY (state) REFERENCES request_state(id),
     FOREIGN KEY (employee_id) REFERENCES employees(id)
);
