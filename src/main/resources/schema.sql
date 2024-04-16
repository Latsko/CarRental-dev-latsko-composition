DROP TABLE IF EXISTS hibernate_sequences;
DROP TABLE IF EXISTS rents;
DROP TABLE IF EXISTS returnals;
DROP TABLE IF EXISTS employees;
DROP TABLE IF EXISTS reservations;
DROP TABLE IF EXISTS cars;
DROP TABLE IF EXISTS clients;
DROP TABLE IF EXISTS branches;
DROP TABLE IF EXISTS car_rental;
DROP TABLE IF EXISTS revenues;
DROP TABLE IF EXISTS users_roles;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS users;

CREATE TABLE car_rental
(
    car_rental_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    domain        VARCHAR(255) NOT NULL,
    address       VARCHAR(255) NOT NULL,
    owner         VARCHAR(255) NOT NULL,
    logo          VARCHAR(255) NOT NULL
);

CREATE TABLE revenues
(
    revenue_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    total_amount DECIMAL(9, 2)
);

CREATE TABLE branches
(
    branch_id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    address       VARCHAR(255) NOT NULL,
    manager_id    BIGINT,
    car_rental_id BIGINT,
    revenue_id    BIGINT,
    FOREIGN KEY (car_rental_id) REFERENCES car_rental (car_rental_id),
    FOREIGN KEY (revenue_id) REFERENCES revenues (revenue_id)
);

CREATE TABLE cars
(
    car_id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    make       VARCHAR(255)  NOT NULL,
    model      VARCHAR(255)  NOT NULL,
    body_style VARCHAR(255)  NOT NULL,
    year       INT           NOT NULL,
    colour     VARCHAR(255)  NOT NULL,
    mileage DOUBLE NOT NULL,
    status     TINYINT       NOT NULL,
    price      DECIMAL(9, 2) NOT NULL,
    branch_id  BIGINT,
    FOREIGN KEY (branch_id) REFERENCES branches (branch_id)
);

CREATE TABLE hibernate_sequences
(
    sequence_name varchar(255) NOT NULL,
    next_val      bigint,
    PRIMARY KEY (sequence_name)
);

CREATE TABLE users
(
    id        BIGINT       NOT NULL,
    login     VARCHAR(255) NOT NULL,
    name      VARCHAR(255),
    password  VARCHAR(255) NOT NULL,
    surname   VARCHAR(255),
    branch_id BIGINT,
    PRIMARY KEY (id)
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
    CONSTRAINT UK_riyp540u0yca4mqdvwmf7vmbv UNIQUE (login),
    CONSTRAINT FK_jb62qduyfqkmnj3w0swbo0jrk FOREIGN KEY (branch_id) REFERENCES branches (branch_id)
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
    CONSTRAINT UK_bxq95ffjulvwwma5nsllkdk6e UNIQUE (login),
    CONSTRAINT FK_cmcru0ilds7m1p8xcqynloon4 FOREIGN KEY (branch_id) REFERENCES branches (branch_id)
);

CREATE TABLE reservations
(
    reservation_id  BIGINT AUTO_INCREMENT PRIMARY KEY,
    start_date      DATE NOT NULL,
    end_date        DATE NOT NULL,
    price           DECIMAL(7, 2),
    start_branch_id BIGINT,
    end_branch_id   BIGINT,
    car_id          BIGINT,
    client_id       BIGINT,
    FOREIGN KEY (start_branch_id) REFERENCES branches (branch_id),
    FOREIGN KEY (end_branch_id) REFERENCES branches (branch_id),
    FOREIGN KEY (car_id) REFERENCES cars (car_id),
    FOREIGN KEY (client_id) REFERENCES clients (id)
);

CREATE TABLE rents
(
    rent_id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    comments       VARCHAR(255) NOT NULL,
    rent_date      DATE         NOT NULL,
    reservation_id BIGINT       NOT NULL,
    employee_id    BIGINT,
    FOREIGN KEY (reservation_id) REFERENCES reservations (reservation_id),
    FOREIGN KEY (employee_id) REFERENCES employees (id)
);

CREATE TABLE returnals
(
    return_id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    comments       VARCHAR(255)  NOT NULL,
    return_date    DATE          NOT NULL,
    upcharge       DECIMAL(7, 2) NOT NULL,
    reservation_id BIGINT        NOT NULL,
    employee_id    BIGINT,
    FOREIGN KEY (reservation_id) REFERENCES reservations (reservation_id),
    FOREIGN KEY (employee_id) REFERENCES employees (id)
);

CREATE TABLE roles
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE users_roles
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (role_id) REFERENCES roles (id)
);
