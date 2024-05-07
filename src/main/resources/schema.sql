DROP TABLE IF EXISTS ID_GEN;
DROP TABLE IF EXISTS rents;
DROP TABLE IF EXISTS returnals;
DROP TABLE IF EXISTS employees;
DROP TABLE IF EXISTS reservations;
DROP TABLE IF EXISTS cars;
DROP TABLE IF EXISTS clients;
DROP TABLE IF EXISTS users_roles;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS branches;
DROP TABLE IF EXISTS car_rental;
DROP TABLE IF EXISTS revenues;

CREATE TABLE car_rental
(
    car_rental_id BIGINT       NOT NULL AUTO_INCREMENT,
    address       VARCHAR(255),
    domain        VARCHAR(255) NOT NULL,
    logo          VARCHAR(255),
    name          VARCHAR(255) NOT NULL,
    owner         VARCHAR(255),
    PRIMARY KEY (car_rental_id)
);

CREATE TABLE revenues
(
    revenue_id   BIGINT NOT NULL AUTO_INCREMENT,
    total_amount DECIMAL(38, 2),
    PRIMARY KEY (revenue_id)
);

CREATE TABLE branches
(
    branch_id     BIGINT NOT NULL AUTO_INCREMENT,
    address       VARCHAR(255),
    manager_id    BIGINT,
    name          VARCHAR(255),
    car_rental_id BIGINT NOT NULL,
    revenue_id    BIGINT,
    PRIMARY KEY (branch_id),
    FOREIGN KEY (car_rental_id) REFERENCES car_rental (car_rental_id),
    FOREIGN KEY (revenue_id) REFERENCES revenues (revenue_id)
);

CREATE TABLE cars
(
    car_id              BIGINT NOT NULL AUTO_INCREMENT,
    body_style          VARCHAR(255),
    colour              VARCHAR(255),
    make                VARCHAR(255),
    mileage             FLOAT  NOT NULL,
    model               VARCHAR(255),
    price               DECIMAL(11, 2),
    status              TINYINT CHECK (status BETWEEN 0 AND 4),
    year_of_manufacture INT    NOT NULL,
    branch_id           BIGINT,
    PRIMARY KEY (car_id),
    FOREIGN KEY (branch_id) REFERENCES branches (branch_id)
);

CREATE TABLE clients
(
    id        BIGINT       NOT NULL,
    login     VARCHAR(255) NOT NULL,
    name      VARCHAR(255),
    password  VARCHAR(255) NOT NULL,
    surname   VARCHAR(255),
    branch_id BIGINT,
    address   VARCHAR(255),
    email     VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (branch_id) REFERENCES branches (branch_id)
);

CREATE TABLE employees
(
    id        BIGINT       NOT NULL,
    login     VARCHAR(255) NOT NULL,
    name      VARCHAR(255),
    password  VARCHAR(255) NOT NULL,
    surname   VARCHAR(255),
    branch_id BIGINT,
    position  TINYINT CHECK (position BETWEEN 0 AND 2),
    PRIMARY KEY (id),
    FOREIGN KEY (branch_id) REFERENCES branches (branch_id)
);

CREATE TABLE reservations
(
    reservation_id  BIGINT NOT NULL AUTO_INCREMENT,
    end_date        DATE   NOT NULL,
    price           DECIMAL(9, 2),
    start_date      DATE   NOT NULL,
    car_id          BIGINT NOT NULL,
    client_id       BIGINT NOT NULL,
    end_branch_id   BIGINT,
    start_branch_id BIGINT,
    PRIMARY KEY (reservation_id),
    FOREIGN KEY (car_id) REFERENCES cars (car_id),
    FOREIGN KEY (client_id) REFERENCES clients (id),
    FOREIGN KEY (end_branch_id) REFERENCES branches (branch_id),
    FOREIGN KEY (start_branch_id) REFERENCES branches (branch_id)
);

CREATE TABLE rents
(
    rent_id        BIGINT NOT NULL AUTO_INCREMENT,
    comments       VARCHAR(255),
    rent_date      DATE,
    employee_id    BIGINT,
    reservation_id BIGINT,
    PRIMARY KEY (rent_id),
    FOREIGN KEY (employee_id) REFERENCES employees (id),
    FOREIGN KEY (reservation_id) REFERENCES reservations (reservation_id)
);

CREATE TABLE returnals
(
    return_id      BIGINT         NOT NULL AUTO_INCREMENT,
    comments       VARCHAR(255),
    return_date    DATE,
    upcharge       DECIMAL(11, 2) NOT NULL,
    employee_id    BIGINT,
    reservation_id BIGINT,
    PRIMARY KEY (return_id),
    FOREIGN KEY (employee_id) REFERENCES employees (id),
    FOREIGN KEY (reservation_id) REFERENCES reservations (reservation_id)
);

CREATE TABLE roles
(
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE users_roles
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    FOREIGN KEY (role_id) REFERENCES roles (id)
);

CREATE TABLE ID_GEN
(
    GEN_NAME VARCHAR(255) NOT NULL PRIMARY KEY,
    GEN_VAL  BIGINT       NOT NULL
);