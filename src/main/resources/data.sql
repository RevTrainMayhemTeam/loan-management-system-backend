INSERT INTO account_role (role_name) VALUES ('Manager');
INSERT INTO account_role (role_name) VALUES ('Customer');

INSERT INTO loan_type ("type") VALUES ('Mortgage');
INSERT INTO loan_type ("type") VALUES ('Auto');
INSERT INTO loan_type ("type") VALUES ('Personal');
INSERT INTO loan_type ("type") VALUES ('Student');
INSERT INTO loan_type ("type") VALUES ('Medical');

INSERT INTO loan_status ("status") VALUES ('Pending');
INSERT INTO loan_status ("status") VALUES ('Approved');
INSERT INTO loan_status ("status") VALUES ('Rejected');

insert into account (email, password, role_id) values ('admin@gmail.com', 'admin', 1);
insert into account (email, password, role_id) values ('user@example.com', 'example', 2);
insert into user_profile (first_name, last_name, phone_number, account_id) values ('Admin', 'User', '5512233445', 1);
insert into user_profile (first_name, last_name, phone_number, account_id) values ('Example', 'User', '5512233446', 2);

INSERT INTO loan (amount, term, user_id, type_id, status_id) VALUES (10000.50, 12, 2, 1, 1);
INSERT INTO loan (amount, term, user_id, type_id, status_id) VALUES (5000.75, 24, 2, 1, 1);
INSERT INTO loan (amount, term, user_id, type_id, status_id) VALUES (15000.00, 36, 2, 1, 1);
INSERT INTO loan (amount, term, user_id, type_id, status_id) VALUES (2500.25, 6, 2, 1, 1);
INSERT INTO loan (amount, term, user_id, type_id, status_id) VALUES (7000.80, 18, 2, 1, 1);
INSERT INTO loan (amount, term, user_id, type_id, status_id) VALUES (20000.00, 48, 2, 1, 1);
INSERT INTO loan (amount, term, user_id, type_id, status_id) VALUES (3000.60, 9, 2, 1, 1);
INSERT INTO loan (amount, term, user_id, type_id, status_id) VALUES (12000.90, 30, 2, 1, 1);
INSERT INTO loan (amount, term, user_id, type_id, status_id) VALUES (8000.40, 15, 2, 1, 1);
INSERT INTO loan (amount, term, user_id, type_id, status_id) VALUES (17000.20, 42, 2, 1, 1);