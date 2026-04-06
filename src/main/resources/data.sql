-- Insert an Admin user
INSERT INTO app_users (fullname, username, password, role)
VALUES ('Joshua Quebrata', 'admin', 'JoshuaAdmin', 'admin');

-- Insert a Guest user
INSERT INTO app_users (fullname, username, password, role)
VALUES ('Juan Dela Cruz', 'guest', 'guest', 'guest');

INSERT INTO app_properties (property_name, property_details, property_type, property_price, is_booked, user_id)
VALUES ('DLSU Condo', 'very cool and spacious i think', 'Studio', '1500', false, 1);

INSERT INTO app_properties (property_name, property_details, property_type, property_price, is_booked, user_id)
VALUES ('ADMU Condo', 'something something', '1 Bedroom', '4800', false, 1);

INSERT INTO app_properties (property_name, property_details, property_type, property_price, is_booked, user_id)
VALUES ('UP Condo', 'lorem ipsum i think', 'Studio', '2300', false, 1);

INSERT INTO app_properties (property_name, property_details, property_type, property_price, is_booked, user_id)
VALUES ('UST Condo', 'bleh blah', '2 Bedroom', '4500', false, 1);

INSERT INTO property_images (img_data, property_id)
VALUES (FILE_READ ('./src/main/resources/sample-img/img1.jpg', NULL), 1);

INSERT INTO property_images (img_data, property_id)
VALUES (FILE_READ ('./src/main/resources/sample-img/img2.jpg', NULL), 2);

INSERT INTO property_images (img_data, property_id)
VALUES (FILE_READ ('./src/main/resources/sample-img/img3.jpg', NULL), 3);

INSERT INTO property_images (img_data, property_id)
VALUES (FILE_READ ('./src/main/resources/sample-img/img4.jpg', NULL), 4);
