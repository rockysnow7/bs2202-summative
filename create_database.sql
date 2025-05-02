CREATE DATABASE IF NOT EXISTS clothingstore;

USE clothingstore;

DROP TABLE IF EXISTS users;
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(40) NOT NULL UNIQUE,
    password_hash VARCHAR(64) NOT NULL,
    user_type ENUM('STANDARD', 'ADMIN') NOT NULL DEFAULT 'STANDARD'
);

DROP TABLE IF EXISTS account_creation_requests;
CREATE TABLE IF NOT EXISTS account_creation_requests (
    request_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(40) NOT NULL UNIQUE,
    password_hash VARCHAR(64) NOT NULL
);

DROP TABLE IF EXISTS items;
CREATE TABLE IF NOT EXISTS items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    item_type ENUM('SHIRT', 'SHOES') NOT NULL,
    name VARCHAR(40) NOT NULL,
    brand VARCHAR(40) NOT NULL,
    size INT NOT NULL,
    colour VARCHAR(40) NOT NULL,
    material VARCHAR(40) NOT NULL,
    date_last_bought DATE NOT NULL,
    stock_quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    image_path VARCHAR(255) NOT NULL
);

DROP TABLE IF EXISTS restock_settings;
CREATE TABLE IF NOT EXISTS restock_settings (
    item_id INT PRIMARY KEY,
    restock_automatically BOOLEAN NOT NULL,
    minimum_stock_quantity INT NOT NULL
);

DROP TABLE IF EXISTS shirts;
CREATE TABLE IF NOT EXISTS shirts (
    item_id INT PRIMARY KEY,
    shirt_type ENUM('T_SHIRT', 'BUTTON_UP') NOT NULL,
    sleeve_type ENUM('SHORT', 'LONG') NOT NULL,
    neck_type ENUM('COLLARED', 'CREW', 'V_NECK') NOT NULL,
    pattern VARCHAR(40),
    num_pockets INT NOT NULL
);

DROP TABLE IF EXISTS t_shirts;
CREATE TABLE IF NOT EXISTS t_shirts (
    item_id INT PRIMARY KEY,
    has_graphic BOOLEAN NOT NULL
);

DROP TABLE IF EXISTS button_up_shirts;
CREATE TABLE IF NOT EXISTS button_up_shirts (
    item_id INT PRIMARY KEY,
    cuff_style ENUM('BARREL', 'FRENCH') NOT NULL
);

DROP TABLE IF EXISTS shoes;
CREATE TABLE IF NOT EXISTS shoes (
    item_id INT PRIMARY KEY,
    shoe_type ENUM('DRESS_SHOES', 'ATHLETIC_SHOES') NOT NULL,
    sole_type ENUM('RUBBER', 'LEATHER') NOT NULL,
    closure_type ENUM('LACES', 'VELCRO', 'SLIP_ON') NOT NULL,
    heel_height ENUM('FLAT', 'LOW', 'HIGH') NOT NULL
);

DROP TABLE IF EXISTS athletic_shoes;
CREATE TABLE IF NOT EXISTS athletic_shoes (
    item_id INT PRIMARY KEY,
    sport VARCHAR(40) NOT NULL
);

DROP TABLE IF EXISTS dress_shoes;
CREATE TABLE IF NOT EXISTS dress_shoes (
    item_id INT PRIMARY KEY,
    toe_style ENUM('ROUND', 'POINTED') NOT NULL
);
