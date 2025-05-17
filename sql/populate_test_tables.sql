USE test;

/* clear all tables */
DELETE FROM users;
DELETE FROM account_creation_requests;
DELETE FROM items;
DELETE FROM restock_settings;
DELETE FROM shirts;
DELETE FROM t_shirts;
DELETE FROM button_up_shirts;
DELETE FROM shoes;
DELETE FROM athletic_shoes;
DELETE FROM dress_shoes;

/* populate users table */
INSERT INTO users (username, password_hash, user_type)
VALUES 
    ('finn', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'ADMIN'), /* password: password */
    ('stan', '1a5e497a2bfa7bfd8aab38a1d576ed882f4a82e855ec610880b4c186ec3f4e73', 'STANDARD'); /* password: stan */

/* populate account_creation_requests table */
INSERT INTO account_creation_requests (username, password_hash)
VALUES
    ('addie', '87e52611cf7e9da1c25eb6bd03b5c88367692da1e1b95605c21ac07c44fb0165'); /* password: addie */

/* populate items table */
INSERT INTO items
VALUES
    (1, 'SHIRT', 'Red T-Shirt', 'Superdry', 10, 'Red', 'Cotton', '2025-04-01', 3, 8.00, 10.00, 't-shirt1.png'),
    (2, 'SHOES', 'Brown Dress Shoes', 'Gucci', 5, 'Brown', 'Leather', '2025-04-20', 1, 80.00, 100.00, 'dress-shoes1.png');

/* populate restock_settings table */
INSERT INTO restock_settings
VALUES
    (1, FALSE, 0),
    (2, FALSE, 0);

/* populate shirts table */
INSERT INTO shirts
VALUES
    (1, 'T_SHIRT', 'SHORT', 'V_NECK', 'Plain', 1);

/* populate t_shirts table */
INSERT INTO t_shirts
VALUES
    (1, FALSE);

/* populate button_up_shirts table */

/* populate shoes table */
INSERT INTO shoes
VALUES
    (2, 'DRESS_SHOES', 'LEATHER', 'LACES', 'LOW');

/* populate athletic_shoes table */

/* populate dress_shoes table */
INSERT INTO dress_shoes
VALUES
    (2, 'ROUND');
