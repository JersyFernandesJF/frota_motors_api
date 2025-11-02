CREATE TABLE properties (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    owner_id BIGINT NOT NULL,
    agency_id BIGINT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    type ENUM('apartment', 'house', 'room', 'office', 'land', 'commercial') NOT NULL,
    status ENUM('for_sale', 'for_rent', 'sold', 'rented') NOT NULL,
    price DECIMAL(15,2) NOT NULL,
    currency CHAR(3) NOT NULL,
    area_m2 DECIMAL(10,2),
    rooms INT,
    bathrooms INT,
    floor INT,
    total_floors INT,
    year_built INT,
    energy_certificate VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT FOREIGN KEY (owner_id) REFERENCES users(id),
    CONSTRAINT FOREIGN KEY (agency_id) REFERENCES agencies(id)
);

CREATE TABLE locations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    property_id BIGINT NOT NULL UNIQUE,
    address VARCHAR(255),
    city VARCHAR(100),
    district VARCHAR(100),
    postal_code VARCHAR(20),
    latitude DECIMAL(10,8),
    longitude DECIMAL(11,8),
    FOREIGN KEY (property_id) REFERENCES properties(id)
);