CREATE TABLE IF NOT EXISTS app_users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fullname VARCHAR(100),
    username VARCHAR(100), -- Fixed 'VARCHAR'
    password VARCHAR(255) NOT NULL,
    role VARCHAR(10) NOT NULL CHECK (role IN ('guest', 'admin')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS app_properties (
    property_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    property_name VARCHAR(100) NOT NULL,
    property_details VARCHAR(100), -- Fixed 'VARCHAR'
    property_type VARCHAR(100) NOT NULL,
    property_price DECIMAL(5,2) NOT NULL,
    is_booked BOOLEAN NOT NULL DEFAULT FALSE,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES app_users(user_id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS property_images (
    img_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    img_data LONGVARBINARY NOT NULL,
    property_id BIGINT NOT NULL,
    CONSTRAINT fk_property_id FOREIGN KEY (property_id) REFERENCES app_properties(property_id) ON DELETE CASCADE
    );