

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
