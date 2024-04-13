INSERT INTO car_rental (name, domain, address, owner, logo)
VALUES ("name", "domain", "address", "owner", "logo");

INSERT INTO revenues (total_amount)
VALUES (0),
       (0),
       (0);

INSERT INTO branches (name, address, car_rental_id, revenue_id)
VALUES ('Krakow Branch', '567 Parkowa Street', 1, 1),
       ('Gdansk Branch', '890 Portowa Avenue', 1, 2),
       ('Warsaw Branch', '234 Sienkiewicza Street', 1, 3);

INSERT INTO cars (make, model, body_style, year, colour, mileage, status, price, branch_id)
VALUES ('Toyota', 'Corolla', 'Sedan', 2023, 'Silver', 15000, 0, 250.00, 1),
       ('Honda', 'Civic', 'Hatchback', 2022, 'Blue', 20000, 1, 220.00, 2),
       ('Ford', 'Mustang', 'Coupe', 2023, 'Red', 12000, 2, 350.00, 3),
       ('Chevrolet', 'Malibu', 'Sedan', 2022, 'Black', 18000, 0, 280.00, 1),
       ('Nissan', 'Altima', 'Sedan', 2023, 'White', 16000, 1, 260.00, 2),
       ('Audi', 'A4', 'Sedan', 2023, 'Gray', 14000, 2, 320.00, 3),
       ('BMW', '3 Series', 'Sedan', 2022, 'Silver', 19000, 0, 380.00, 1),
       ('Mercedes-Benz', 'C-Class', 'Sedan', 2023, 'Black', 17000, 1, 400.00, 2),
       ('Volkswagen', 'Passat', 'Sedan', 2022, 'Blue', 15000, 2, 270.00, 3),
       ('Hyundai', 'Elantra', 'Sedan', 2023, 'Red', 13000, 0, 240.00, 1),
       ('Tesla', 'Model S', 'Sedan', 2023, 'White', 10000, 1, 600.00, 2),
       ('Kia', 'Seltos', 'SUV', 2022, 'Gray', 17000, 2, 230.00, 3),
       ('Subaru', 'Outback', 'Wagon', 2023, 'Green', 12000, 0, 270.00, 1),
       ('Mazda', 'CX-5', 'SUV', 2022, 'Red', 14000, 1, 260.00, 2),
       ('Jeep', 'Wrangler', 'SUV', 2023, 'Black', 9000, 2, 350.00, 3),
       ('Lexus', 'RX', 'SUV', 2022, 'Silver', 16000, 0, 400.00, 1),
       ('Infiniti', 'Q50', 'Sedan', 2023, 'Blue', 11000, 1, 320.00, 2),
       ('Volvo', 'XC60', 'SUV', 2022, 'White', 13000, 2, 380.00, 3),
       ('Porsche', '911', 'Coupe', 2023, 'Red', 8000, 0, 1200.00, 1),
       ('Ferrari', '488 GTB', 'Coupe', 2022, 'Yellow', 5000, 1, 2500.00, 2);

INSERT INTO users(id, login, password, name, surname, branch_id)
VALUES (1, 'jarek', '$2a$10$JFtdUV4p3CbvLJxHlhvlkeYXyhI7d84yoS33Cp.EYvfcACRmUhOIK', 'John', 'Doe', 1),
       (2, 'daniel', '$2a$10$lcSkYMyBmW0G.4hOVLjsBe3Vke5KZ/3q4xvk32uEGb6c4XIF6Ma2y', 'Jane', 'Smith', 2),
       (3, 'piotr', '$2a$10$C6J0iO5dt4i44/.mz6onxeCAmU7tTFyMRyueLsEnx7mYiL15wd9ZK', 'Alice', 'Johnson', 3),
       (4, 'marek', '$2a$10$SURgxM8zxzqksrtfXdN.2.6.83R9bUP2bNzpQemVGS6OE/gHsDR1C', 'Bob', 'Williams', 1),
       (5, 'employee1', '$2a$10$SURgxM8zxzqksrtfXdN.2.6.83R9bUP2bNzpQemVGS6OE/gHsDR1C', 'Emma', 'Brown', 2),
       (6, 'employee2', '$2a$10$SURgxM8zxzqksrtfXdN.2.6.83R9bUP2bNzpQemVGS6OE/gHsDR1C', 'Michael', 'Jones', 3),
       (7, 'employee3', '$2a$10$SURgxM8zxzqksrtfXdN.2.6.83R9bUP2bNzpQemVGS6OE/gHsDR1C', 'Olivia', 'Davis', 1),
       (8, 'employee4', '$2a$10$SURgxM8zxzqksrtfXdN.2.6.83R9bUP2bNzpQemVGS6OE/gHsDR1C', 'William', 'Garcia', 2),
       (9, 'employee5', '$2a$10$SURgxM8zxzqksrtfXdN.2.6.83R9bUP2bNzpQemVGS6OE/gHsDR1C', 'Sophia', 'Miller', 3),
       (10, 'employee6', '$2a$10$SURgxM8zxzqksrtfXdN.2.6.83R9bUP2bNzpQemVGS6OE/gHsDR1C', 'Liam', 'Martinez', 1),
       (11, 'employee7', '$2a$10$SURgxM8zxzqksrtfXdN.2.6.83R9bUP2bNzpQemVGS6OE/gHsDR1C', 'Ava', 'Wilson', 2),
       (12, 'employee8', '$2a$10$SURgxM8zxzqksrtfXdN.2.6.83R9bUP2bNzpQemVGS6OE/gHsDR1C', 'James', 'Anderson', 3),
       (13, 'employee9', '$2a$10$SURgxM8zxzqksrtfXdN.2.6.83R9bUP2bNzpQemVGS6OE/gHsDR1C', 'Ella', 'Taylor', 1),
       (14, 'employee10', '$2a$10$SURgxM8zxzqksrtfXdN.2.6.83R9bUP2bNzpQemVGS6OE/gHsDR1C', 'Alexander', 'Clark', 2),
       (15, 'employee11', '$2a$10$SURgxM8zxzqksrtfXdN.2.6.83R9bUP2bNzpQemVGS6OE/gHsDR1C', 'Grace', 'Hill', 3),
       (16, 'employee12', '$2a$10$SURgxM8zxzqksrtfXdN.2.6.83R9bUP2bNzpQemVGS6OE/gHsDR1C', 'Mia', 'Scott', 1),
       (17, 'employee13', '$2a$10$SURgxM8zxzqksrtfXdN.2.6.83R9bUP2bNzpQemVGS6OE/gHsDR1C', 'Benjamin', 'Green', 2),
       (18, 'employee14', '$2a$10$SURgxM8zxzqksrtfXdN.2.6.83R9bUP2bNzpQemVGS6OE/gHsDR1C', 'Chloe', 'Adams', 3),
       (19, 'employee15', '$2a$10$SURgxM8zxzqksrtfXdN.2.6.83R9bUP2bNzpQemVGS6OE/gHsDR1C', 'Lucas', 'Wright', 1),
       (20, 'employee16', '$2a$10$SURgxM8zxzqksrtfXdN.2.6.83R9bUP2bNzpQemVGS6OE/gHsDR1C', 'Madison', 'Baker', 2),
       (21, 'employee17', '$2a$10$SURgxM8zxzqksrtfXdN.2.6.83R9bUP2bNzpQemVGS6OE/gHsDR1C','Sophie', 'Bennett', 1),
       (22, 'employee18', '$2a$10$SURgxM8zxzqksrtfXdN.2.6.83R9bUP2bNzpQemVGS6OE/gHsDR1C','Jacob', 'Fisher', 2),
       (23, 'employee19', '$2a$10$SURgxM8zxzqksrtfXdN.2.6.83R9bUP2bNzpQemVGS6OE/gHsDR1C','Nora', 'Clarkson', 3),
       (24, 'employee20', '$2a$10$SURgxM8zxzqksrtfXdN.2.6.83R9bUP2bNzpQemVGS6OE/gHsDR1C','Gabriel', 'Powell', 1),
       (25, 'employee21', '$2a$10$SURgxM8zxzqksrtfXdN.2.6.83R9bUP2bNzpQemVGS6OE/gHsDR1C','Lillian', 'Roberts', 2),
       (26, 'employee22', '$2a$10$SURgxM8zxzqksrtfXdN.2.6.83R9bUP2bNzpQemVGS6OE/gHsDR1C','Caleb', 'Hughes', 3),
       (27, 'employee23', '$2a$10$SURgxM8zxzqksrtfXdN.2.6.83R9bUP2bNzpQemVGS6OE/gHsDR1C','Violet', 'Harrison', 1),
       (28, 'employee24', '$2a$10$SURgxM8zxzqksrtfXdN.2.6.83R9bUP2bNzpQemVGS6OE/gHsDR1C','Levi', 'Foster', 2),
       (29, 'employee25', '$2a$10$SURgxM8zxzqksrtfXdN.2.6.83R9bUP2bNzpQemVGS6OE/gHsDR1C','Stella', 'Murray', 3),
       (30, 'employee26', '$2a$10$SURgxM8zxzqksrtfXdN.2.6.83R9bUP2bNzpQemVGS6OE/gHsDR1C','Hudson', 'Simmons', 1);

INSERT INTO employees (id, position)
VALUES (1, 1),
       (2, 1),
       (3, 0),
       (4, 1),
       (5, 0),
       (6, 1),
       (7, 0),
       (8, 1),
       (9, 0),
       (10, 1),
       (11, 0),
       (12, 1),
       (13, 0),
       (14, 1),
       (15, 0),
       (16, 1),
       (17, 0),
       (18, 1),
       (19, 0),
       (20, 1);

INSERT INTO clients (id, address, email)
VALUES (21, '111 Hill St', 'sophie@example.com'),
       (22, '222 River St', 'jacob@example.com'),
       (23, '333 Oak St', 'nora@example.com'),
       (24, '444 Pine St', 'gabriel@example.com'),
       (25, '555 Elm St', 'lillian@example.com'),
       (26, '666 Birch St', 'caleb@example.com'),
       (27, '777 Spruce St', 'violet@example.com'),
       (28, '888 Cedar St', 'levi@example.com'),
       (29, '999 Walnut St', 'stella@example.com'),
       (30, '101 Cherry St', 'hudson@example.com');


INSERT INTO reservations (start_date, end_date, price, start_branch_id, end_branch_id, car_id, client_id)
VALUES ('2024-01-10', '2024-01-15', 350.00, 1, 2, 1, 21),
       ('2024-01-12', '2024-01-20', 400.00, 2, 3, 2, 22),
       ('2024-01-14', '2024-01-18', 300.00, 3, 1, 3, 23),
       ('2024-01-16', '2024-01-22', 450.00, 1, 2, 4, 24),
       ('2024-01-18', '2024-01-25', 500.00, 2, 3, 5, 25),
       ('2024-01-20', '2024-01-27', 380.00, 3, 1, 6, 26),
       ('2024-01-22', '2024-01-29', 420.00, 1, 2, 7, 27),
       ('2024-01-24', '2024-01-31', 480.00, 2, 3, 8, 28),
       ('2024-01-26', '2024-02-02', 520.00, 3, 1, 9, 29),
       ('2024-01-28', '2024-02-04', 390.00, 1, 2, 10, 30);

INSERT INTO rents (comments, rent_date, reservation_id, employee_id)
VALUES ('Good condition', '2024-01-10', 1, 1),
       ('Needs cleaning', '2024-01-12', 2, 2),
       ('Smooth ride', '2024-01-14', 3, 3),
       ('Great experience', '2024-01-16', 4, 4),
       ('Minor issue with AC', '2024-01-18', 5, 5),
       ('Comfortable interior', '2024-01-20', 6, 6),
       ('Excellent mileage', '2024-01-22', 7, 7),
       ('Fantastic handling', '2024-01-24', 8, 8);

INSERT INTO returnals (comments, return_date, upcharge, reservation_id, employee_id)
VALUES ('Clean and tidy', '2024-01-15', 50.00, 1, 1),
       ('Minor scratches', '2024-01-17', 30.00, 2, 2),
       ('Fuel tank full', '2024-01-19', 20.00, 3, 3),
       ('Smooth return', '2024-01-21', 10.00, 4, 4),
       ('Needs cleaning', '2024-01-23', 40.00, 5, 5),
       ('Issues with brakes', '2024-01-25', 60.00, 6, 6),
       ('Excellent condition', '2024-01-27', 15.00, 7, 7),
       ('Great experience', '2024-01-29', 25.00, 8, 8);

-- Authentication and authorization

INSERT INTO roles(id, name)
VALUES (1, 'ROLE_CLIENT'),
       (2, 'ROLE_EMPLOYEE'),
       (3, 'ROLE_ADMIN'),
       (4, 'ROLE_MANAGER');

INSERT INTO users_roles(user_id, role_id)
VALUES (1, 2),
       (2, 3),
       (3, 1),
       (4, 4);
