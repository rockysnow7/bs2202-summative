USE clothingstore;

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
    (2, 'SHIRT', 'Fancy Button-Up', 'Gucci', 6, 'Beige', 'Cotton', '2025-05-20', 1, 100.00, 120.00, 'button-up1.png'),
    (3, 'SHOES', 'Brown Dress Shoes', 'Gucci', 5, 'Brown', 'Leather', '2025-04-20', 1, 80.00, 100.00, 'dress-shoes1.png'),
    (4, 'SHOES', 'Running Shoes', 'Nike', 10, 'Black', 'Rubber', '2025-05-20', 2, 45.00, 50.00, 'running-shoes1.png');

/* populate restock_settings table */
INSERT INTO restock_settings
VALUES
    (1, FALSE, 0),
    (2, FALSE, 0),
    (3, FALSE, 0),
    (4, FALSE, 0);

/* populate shirts table */
INSERT INTO shirts
VALUES
    (1, 'T_SHIRT', 'SHORT', 'V_NECK', 'Plain', 1),
    (2, 'BUTTON_UP', 'LONG', 'COLLARED', 'Plaid', 2);

/* populate t_shirts table */
INSERT INTO t_shirts
VALUES
    (1, FALSE);

/* populate button_up_shirts table */
INSERT INTO button_up_shirts
VALUES
    (2, 'FRENCH');

/* populate shoes table */
INSERT INTO shoes
VALUES
    (3, 'DRESS_SHOES', 'LEATHER', 'LACES', 'LOW'),
    (4, 'ATHLETIC_SHOES', 'RUBBER', 'LACES', 'LOW');

/* populate athletic_shoes table */
INSERT INTO athletic_shoes
VALUES
    (4, 'Running');

/* populate dress_shoes table */
INSERT INTO dress_shoes
VALUES
    (3, 'ROUND');
