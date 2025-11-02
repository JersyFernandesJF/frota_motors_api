CREATE TABLE media (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    property_id BIGINT NOT NULL,
    media_type ENUM('photo', 'video', 'floorplan', 'virtual_tour') NOT NULL,
    url VARCHAR(500) NOT NULL,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT FOREIGN KEY (property_id) REFERENCES properties(id)
);

CREATE TABLE favorites (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    property_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT FOREIGN KEY (property_id) REFERENCES properties(id),
    UNIQUE (user_id, property_id)
)