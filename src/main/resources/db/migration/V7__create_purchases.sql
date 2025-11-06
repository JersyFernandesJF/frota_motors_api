CREATE TABLE purchases (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    buyer_id UUID NOT NULL,
    type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    vehicle_id UUID,
    part_id UUID,
    price DECIMAL(15,2) NOT NULL,
    currency CHAR(3) NOT NULL,
    quantity INTEGER DEFAULT 1,
    total_amount DECIMAL(15,2) NOT NULL,
    shipping_address TEXT,
    delivery_date TIMESTAMP,
    tracking_number VARCHAR(100),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_purchase_buyer FOREIGN KEY (buyer_id) REFERENCES users(id),
    CONSTRAINT fk_purchase_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(id),
    CONSTRAINT fk_purchase_part FOREIGN KEY (part_id) REFERENCES parts(id)
);

CREATE INDEX idx_purchases_buyer ON purchases(buyer_id);
CREATE INDEX idx_purchases_status ON purchases(status);
CREATE INDEX idx_purchases_type ON purchases(type);
CREATE INDEX idx_purchases_vehicle ON purchases(vehicle_id);
CREATE INDEX idx_purchases_part ON purchases(part_id);

