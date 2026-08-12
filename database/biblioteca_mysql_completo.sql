-- ============================================================
-- BIBLIOTECA MICROSERVICIOS - MYSQL 8.x
-- Crea 5 bases independientes, usuarios y tablas.
-- Ejecutar como root/administrador de MySQL.
-- ============================================================

CREATE DATABASE IF NOT EXISTS auth_db CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
CREATE DATABASE IF NOT EXISTS catalog_db CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
CREATE DATABASE IF NOT EXISTS loan_db CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
CREATE DATABASE IF NOT EXISTS reservation_db CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
CREATE DATABASE IF NOT EXISTS review_db CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

CREATE USER IF NOT EXISTS 'auth_user'@'%' IDENTIFIED BY 'auth_password';
CREATE USER IF NOT EXISTS 'catalog_user'@'%' IDENTIFIED BY 'catalog_password';
CREATE USER IF NOT EXISTS 'loan_user'@'%' IDENTIFIED BY 'loan_password';
CREATE USER IF NOT EXISTS 'reservation_user'@'%' IDENTIFIED BY 'reservation_password';
CREATE USER IF NOT EXISTS 'review_user'@'%' IDENTIFIED BY 'review_password';

ALTER USER 'auth_user'@'%' IDENTIFIED BY 'auth_password';
ALTER USER 'catalog_user'@'%' IDENTIFIED BY 'catalog_password';
ALTER USER 'loan_user'@'%' IDENTIFIED BY 'loan_password';
ALTER USER 'reservation_user'@'%' IDENTIFIED BY 'reservation_password';
ALTER USER 'review_user'@'%' IDENTIFIED BY 'review_password';

GRANT ALL PRIVILEGES ON auth_db.* TO 'auth_user'@'%';
GRANT ALL PRIVILEGES ON catalog_db.* TO 'catalog_user'@'%';
GRANT ALL PRIVILEGES ON loan_db.* TO 'loan_user'@'%';
GRANT ALL PRIVILEGES ON reservation_db.* TO 'reservation_user'@'%';
GRANT ALL PRIVILEGES ON review_db.* TO 'review_user'@'%';
FLUSH PRIVILEGES;

USE auth_db;
CREATE TABLE IF NOT EXISTS users (
    id BINARY(16) NOT NULL,
    full_name VARCHAR(120) NOT NULL,
    email VARCHAR(160) NOT NULL,
    password_hash VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    active BOOLEAN NOT NULL,
    created_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_users_email UNIQUE (email)
) ENGINE=InnoDB;

USE catalog_db;
CREATE TABLE IF NOT EXISTS books (
    id BINARY(16) NOT NULL,
    isbn VARCHAR(20) NOT NULL,
    title VARCHAR(180) NOT NULL,
    author VARCHAR(140) NOT NULL,
    category VARCHAR(100) NOT NULL,
    description VARCHAR(1000) NULL,
    total_copies INT NOT NULL,
    available_copies INT NOT NULL,
    active BOOLEAN NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_books_isbn UNIQUE (isbn)
) ENGINE=InnoDB;

USE loan_db;
CREATE TABLE IF NOT EXISTS loans (
    id BINARY(16) NOT NULL,
    user_id BINARY(16) NOT NULL,
    user_email VARCHAR(160) NOT NULL,
    book_id BINARY(16) NOT NULL,
    book_title VARCHAR(180) NOT NULL,
    borrowed_at DATE NOT NULL,
    due_date DATE NOT NULL,
    returned_at DATE NULL,
    status VARCHAR(20) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    INDEX idx_loans_user_status (user_id, status),
    INDEX idx_loans_book_status (book_id, status)
) ENGINE=InnoDB;

USE reservation_db;
CREATE TABLE IF NOT EXISTS reservations (
    id BINARY(16) NOT NULL,
    user_id BINARY(16) NOT NULL,
    user_email VARCHAR(160) NOT NULL,
    book_id BINARY(16) NOT NULL,
    book_title VARCHAR(180) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    expires_at DATETIME(6) NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    INDEX idx_reservations_user_status (user_id, status),
    INDEX idx_reservations_book_status (book_id, status)
) ENGINE=InnoDB;

USE review_db;
CREATE TABLE IF NOT EXISTS reviews (
    id BINARY(16) NOT NULL,
    user_id BINARY(16) NOT NULL,
    user_email VARCHAR(160) NOT NULL,
    book_id BINARY(16) NOT NULL,
    book_title VARCHAR(180) NOT NULL,
    rating INT NOT NULL,
    comment VARCHAR(1000) NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT uk_reviews_user_book UNIQUE (user_id, book_id),
    INDEX idx_reviews_book (book_id),
    CONSTRAINT chk_reviews_rating CHECK (rating BETWEEN 1 AND 5)
) ENGINE=InnoDB;
