CREATE TABLE IF NOT EXISTS users (
                                     user_id INT AUTO_INCREMENT PRIMARY KEY,
                                     first_name VARCHAR(100),
    last_name VARCHAR(100),
    email VARCHAR(150) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20),
    role VARCHAR(10) NOT NULL CHECK (role IN ('guest', 'admin')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS payment_accounts (
                                                account_id INT AUTO_INCREMENT PRIMARY KEY,
                                                admin_id INT NOT NULL,
                                                method ENUM('GCash','Maya','QRPh') NOT NULL,
    account_name VARCHAR(100),
    account_number VARCHAR(50),
    paymongo_source_id VARCHAR(100),
    paymongo_public_key VARCHAR(100),
    paymongo_secret_key VARCHAR(100),
    qr_image LONGBLOB,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_admin FOREIGN KEY (admin_id) REFERENCES users(user_id)
    );

CREATE TABLE IF NOT EXISTS properties (
                                          property_id INT AUTO_INCREMENT PRIMARY KEY,
                                          admin_id INT,
                                          property_name VARCHAR(150),
    description TEXT,
    address VARCHAR(255),
    price_per_night DECIMAL(10,2),
    max_guests INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_property_admin FOREIGN KEY (admin_id) REFERENCES users(user_id)
    );

CREATE TABLE IF NOT EXISTS property_photos (
                                               photo_id INT AUTO_INCREMENT PRIMARY KEY,
                                               property_id INT,
                                               photo_url VARCHAR(255),
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_photo_property FOREIGN KEY (property_id) REFERENCES properties(property_id)
    );

CREATE TABLE IF NOT EXISTS bookings (
                                        booking_id INT AUTO_INCREMENT PRIMARY KEY,
                                        property_id INT,
                                        guest_id INT,
                                        check_in_date DATE,
                                        check_out_date DATE,
                                        total_price DECIMAL(10,2),
    booking_status ENUM('pending','confirmed','cancelled'),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_booking_property FOREIGN KEY (property_id) REFERENCES properties(property_id),
    CONSTRAINT fk_booking_guest FOREIGN KEY (guest_id) REFERENCES users(user_id)
    );

CREATE TABLE IF NOT EXISTS payments (
                                        payment_id INT AUTO_INCREMENT PRIMARY KEY,
                                        booking_id INT,
                                        account_id INT,
                                        amount DECIMAL(10,2),
    payment_method VARCHAR(50),
    payment_status ENUM('pending','completed','failed'),
    paymongo_payment_intent_id VARCHAR(100),
    paymongo_source_id VARCHAR(100),
    paymongo_status VARCHAR(50),
    transaction_reference VARCHAR(255),
    payment_date TIMESTAMP,
    CONSTRAINT fk_payment_booking FOREIGN KEY (booking_id) REFERENCES bookings(booking_id),
    CONSTRAINT fk_payment_account FOREIGN KEY (account_id) REFERENCES payment_accounts(account_id)
    );

CREATE TABLE IF NOT EXISTS reviews (
                                       review_id INT AUTO_INCREMENT PRIMARY KEY,
                                       property_id INT,
                                       guest_id INT,
                                       rating INT,
                                       comment TEXT,
                                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       CONSTRAINT fk_review_property FOREIGN KEY (property_id) REFERENCES properties(property_id),
    CONSTRAINT fk_review_guest FOREIGN KEY (guest_id) REFERENCES users(user_id)
    );

CREATE TABLE IF NOT EXISTS messages (
                                        message_id INT AUTO_INCREMENT PRIMARY KEY,
                                        sender_id INT,
                                        receiver_id INT,
                                        message_text TEXT,
                                        sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                        read_status BOOLEAN DEFAULT FALSE,
                                        CONSTRAINT fk_message_sender FOREIGN KEY (sender_id) REFERENCES users(user_id),
    CONSTRAINT fk_message_receiver FOREIGN KEY (receiver_id) REFERENCES users(user_id)
    );

CREATE TABLE IF NOT EXISTS system_logs (
                                           log_id INT AUTO_INCREMENT PRIMARY KEY,
                                           user_id INT,
                                           action VARCHAR(255),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(50),
    CONSTRAINT fk_log_user FOREIGN KEY (user_id) REFERENCES users(user_id)
    );