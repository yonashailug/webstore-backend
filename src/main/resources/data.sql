INSERT INTO roles(id, name) VALUES(1,'ADMIN');
INSERT INTO roles(id, name) VALUES(2,'SELLER');
INSERT INTO roles(id, name) VALUES(3,'BUYER');

INSERT into users (id, name, username, password, is_enabled, email)  VALUES (1, 'Seller','seller','$2a$10$2BlJ/vCO2Age4iolJF/PPOZVMjBD74UaOLGKhZTsqjyvYUrLMwqqW',true, 'seller@gmail.com');
INSERT into users (id, name, username, password, is_enabled, email)  VALUES (2, 'Buyer','buyer','$2a$10$2BlJ/vCO2Age4iolJF/PPOZVMjBD74UaOLGKhZTsqjyvYUrLMwqqW',true, 'buyer@gmail.com');
INSERT into users (id, name, username, password, is_enabled, email)  VALUES (3, 'Admin','admin','$2a$10$2BlJ/vCO2Age4iolJF/PPOZVMjBD74UaOLGKhZTsqjyvYUrLMwqqW',true, 'admin@gmail.com');

INSERT INTO user_roles(user_id, role_id) VALUES(1, 2);
INSERT INTO user_roles(user_id, role_id) VALUES(2, 3);
INSERT INTO user_roles(user_id, role_id) VALUES(3, 1);

INSERT into product (id, name, price, quantity, category, description, seller_id, discount, tax)  VALUES (1, 'iPhone 13',1200,3,'phones', 'Brand new iphone 13', 2, 0,0);
INSERT into product (id, name, price, quantity, category, description, seller_id, discount, tax)  VALUES (2, 'iPhone 12',900,3,'phones', 'Brand new iphone 12', 2, 0,0);
INSERT into product (id, name, price, quantity, category, description, seller_id, discount, tax)  VALUES (3, 'iPhone 11',800,3,'phones', 'Brand new iphone 11', 2, 0,0);