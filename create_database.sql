CREATE DATABASE IF NOT EXISTS clothingstore;

USE clothingstore;

CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(40) NOT NULL UNIQUE,
    password_hash VARCHAR(64) NOT NULL,
    user_type ENUM('STANDARD', 'ADMIN') NOT NULL DEFAULT 'STANDARD'
);

CREATE TABLE IF NOT EXISTS account_creation_requests (
    request_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(40) NOT NULL UNIQUE,
    password_hash VARCHAR(64) NOT NULL
);
