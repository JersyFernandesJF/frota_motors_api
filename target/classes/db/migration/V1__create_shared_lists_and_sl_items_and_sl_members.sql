CREATE TABLE shared_lists (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    owner_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT FOREIGN KEY (owner_id) REFERENCES users(id)
);

CREATE TABLE shared_list_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    list_id BIGINT NOT NULL,
    property_id BIGINT NOT NULL,
    CONSTRAINT FOREIGN KEY (list_id) REFERENCES shared_lists(id),
    CONSTRAINT FOREIGN KEY (property_id) REFERENCES properties(id),
    UNIQUE (list_id, property_id)
);

CREATE TABLE shared_list_members (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    list_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT FOREIGN KEY (list_id) REFERENCES shared_lists(id),
    CONSTRAINT FOREIGN KEY (user_id) REFERENCES users(id),
    UNIQUE (list_id, user_id)
);