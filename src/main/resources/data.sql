INSERT INTO car_rental (name, domain, address, owner, logo)
VALUES ('name', 'domain', 'address', 'owner', 'logo');

INSERT INTO revenues (total_amount)
VALUES (0),
       (0),
       (0);

INSERT INTO branches (name, address, car_rental_id, revenue_id)
VALUES ('Krakow Branch', '567 Parkowa Street', 1, 1),
       ('Gdansk Branch', '890 Portowa Avenue', 1, 2),
       ('Warsaw Branch', '234 Sienkiewicza Street', 1, 3);

INSERT INTO cars (make, model, body_style, year_of_manufacture, colour, mileage, status, price, branch_id)
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

INSERT INTO roles(id, name)
VALUES (1, 'ROLE_ADMIN'),
       (2, 'ROLE_MANAGER'),
       (3, 'ROLE_EMPLOYEE'),
       (4, 'ROLE_CLIENT');

INSERT INTO employees (id, login, name, password, surname, branch_id, position)
VALUES (1, 'admin', 'Emma', '$2a$10$q2NyK/W0sEdi4tbgYjOx/u6YTkDAloPMPTLCTlGewONfzW.U.E1.m', 'Brown', 2, 1),
       (2, 'manager', 'Michael', '$2a$10$KhB6m4q/.QMXe2Zh74RnQeUw8/ikRvXvRaNhu.uZl5DNpHtkQXdmW', 'Jones', 3, 2),
       (3, 'employee', 'Olivia', '$2a$10$Minluq0Dyag/VJo8i9prKO7W64ozNZO9wkkq6LEu/b1KIuRejDoTS', 'Davis', 1, 0),
       (4, 'employee4', 'William', '$2a$10$Minluq0Dyag/VJo8i9prKO7W64ozNZO9wkkq6LEu/b1KIuRejDoTS', 'Garcia', 2, 1),
       (5, 'employee5', 'Sophia', '$2a$10$Minluq0Dyag/VJo8i9prKO7W64ozNZO9wkkq6LEu/b1KIuRejDoTS', 'Miller', 3, 2),
       (6, 'employee6', 'Liam', '$2a$10$Minluq0Dyag/VJo8i9prKO7W64ozNZO9wkkq6LEu/b1KIuRejDoTS', 'Martinez', 1, 0),
       (7, 'employee7', 'Ava', '$2a$10$Minluq0Dyag/VJo8i9prKO7W64ozNZO9wkkq6LEu/b1KIuRejDoTS', 'Wilson', 2, 1),
       (8, 'employee8', 'James', '$2a$10$Minluq0Dyag/VJo8i9prKO7W64ozNZO9wkkq6LEu/b1KIuRejDoTS', 'Anderson', 3, 0),
       (9, 'employee9', 'Ella', '$2a$10$Minluq0Dyag/VJo8i9prKO7W64ozNZO9wkkq6LEu/b1KIuRejDoTS', 'Taylor', 1, 1),
       (10, 'employee10', 'Alexander', '$2a$10$Minluq0Dyag/VJo8i9prKO7W64ozNZO9wkkq6LEu/b1KIuRejDoTS', 'Clark', 2, 2),
       (11, 'employee11', 'Grace', '$2a$10$Minluq0Dyag/VJo8i9prKO7W64ozNZO9wkkq6LEu/b1KIuRejDoTS', 'Hill', 3, 0),
       (12, 'employee12', 'Mia', '$2a$10$Minluq0Dyag/VJo8i9prKO7W64ozNZO9wkkq6LEu/b1KIuRejDoTS', 'Scott', 1, 1),
       (13, 'employee13', 'Benjamin', '$2a$10$Minluq0Dyag/VJo8i9prKO7W64ozNZO9wkkq6LEu/b1KIuRejDoTS', 'Green', 2, 2),
       (14, 'employee14', 'Chloe', '$2a$10$Minluq0Dyag/VJo8i9prKO7W64ozNZO9wkkq6LEu/b1KIuRejDoTS', 'Adams', 3, 0),
       (15, 'employee15', 'Lucas', '$2a$10$Minluq0Dyag/VJo8i9prKO7W64ozNZO9wkkq6LEu/b1KIuRejDoTS', 'Wright', 1, 1);

INSERT INTO clients (id, login, name, password, surname, branch_id, address, email)
VALUES (16, 'client', 'John', '$2a$10$HY1dLwMauKZ91FgixtS5E.3OnGW9XNjU9NUPn3OhjPW89hQ38xtnC', 'Doe', 1, '123 Main St, Anytown, USA', 'john.doe@example.com'),
       (17, 'client2', 'Jane', '$2a$10$HY1dLwMauKZ91FgixtS5E.3OnGW9XNjU9NUPn3OhjPW89hQ38xtnC', 'Smith', 2, '456 Elm St, Othertown, USA', 'jane.smith@example.com'),
       (18, 'client3', 'Alice', '$2a$10$HY1dLwMauKZ91FgixtS5E.3OnGW9XNjU9NUPn3OhjPW89hQ38xtnC', 'Johnson', 3, '789 Oak St, Anothertown, USA', 'alice.johnson@example.com'),
       (19, 'client4', 'Bob', '$2a$10$HY1dLwMauKZ91FgixtS5E.3OnGW9XNjU9NUPn3OhjPW89hQ38xtnC', 'Williams', 1, '321 Maple St, Yetanothertown, USA', 'bob.williams@example.com'),
       (20, 'client5', 'Sophie', '$2a$10$HY1dLwMauKZ91FgixtS5E.3OnGW9XNjU9NUPn3OhjPW89hQ38xtnC', 'Bennett', 1, '100 Pine St, Newville, USA', 'sophie.bennett@example.com'),
       (21, 'client6', 'Sophie', '$2a$10$HY1dLwMauKZ91FgixtS5E.3OnGW9XNjU9NUPn3OhjPW89hQ38xtnC', 'Bennett', 1, '100 Pine St, Newville, USA', 'sophie.bennett@example.com'),
       (22, 'client7', 'Jacob', '$2a$10$HY1dLwMauKZ91FgixtS5E.3OnGW9XNjU9NUPn3OhjPW89hQ38xtnC', 'Fisher', 2, '200 Cedar St, Nexttown, USA', 'jacob.fisher@example.com'),
       (23, 'client8', 'Nora', '$2a$10$HY1dLwMauKZ91FgixtS5E.3OnGW9XNjU9NUPn3OhjPW89hQ38xtnC', 'Clarkson', 3, '300 Birch St, Lasttown, USA', 'nora.clarkson@example.com'),
       (24, 'client9', 'Gabriel', '$2a$10$HY1dLwMauKZ91FgixtS5E.3OnGW9XNjU9NUPn3OhjPW89hQ38xtnC', 'Powell', 1, '400 Spruce St, Firsttown, USA', 'gabriel.powell@example.com'),
       (25, 'client10', 'Lillian', '$2a$10$HY1dLwMauKZ91FgixtS5E.3OnGW9XNjU9NUPn3OhjPW89hQ38xtnC', 'Roberts', 2, '500 Fir St, Secondtown, USA', 'lillian.roberts@example.com'),
       (26, 'client11', 'Caleb', '$2a$10$HY1dLwMauKZ91FgixtS5E.3OnGW9XNjU9NUPn3OhjPW89hQ38xtnC', 'Hughes', 3, '600 Pinecone St, Thirdtown, USA', 'caleb.hughes@example.com'),
       (27, 'client12', 'Violet', '$2a$10$HY1dLwMauKZ91FgixtS5E.3OnGW9XNjU9NUPn3OhjPW89hQ38xtnC', 'Harrison', 1, '700 Pineapple St, Fourthtown, USA', 'violet.harrison@example.com'),
       (28, 'client13', 'Levi', '$2a$10$HY1dLwMauKZ91FgixtS5E.3OnGW9XNjU9NUPn3OhjPW89hQ38xtnC', 'Foster', 2, '800 Pinesap St, Fifthtown, USA', 'levi.foster@example.com'),
       (29, 'client14', 'Stella', '$2a$10$HY1dLwMauKZ91FgixtS5E.3OnGW9XNjU9NUPn3OhjPW89hQ38xtnC', 'Murray', 3, '900 Pinebark St, Sixthtown, USA', 'stella.murray@example.com'),
       (30, 'client15', 'Hudson', '$$2a$10$HY1dLwMauKZ91FgixtS5E.3OnGW9XNjU9NUPn3OhjPW89hQ38xtnC', 'Simmons', 1, '1000 Pineneedle St, Seventhtown, USA', 'hudson.simmons@example.com');

INSERT INTO users_roles(user_id, role_id)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (16, 4);

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

