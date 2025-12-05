DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'parts') THEN
        CREATE TABLE parts (
            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
            seller_id UUID NOT NULL,
            agency_id UUID,
            category VARCHAR(50) NOT NULL,
            status VARCHAR(50) NOT NULL,
            name VARCHAR(200) NOT NULL,
            description TEXT,
            price DECIMAL(15,2) NOT NULL,
            currency CHAR(3) NOT NULL,
            part_number VARCHAR(100),
            oem_number VARCHAR(100),
            brand VARCHAR(100),
            compatible_vehicles TEXT,
            condition_type VARCHAR(50),
            quantity_available INTEGER,
            warranty_months INTEGER,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            CONSTRAINT fk_part_seller FOREIGN KEY (seller_id) REFERENCES users(id),
            CONSTRAINT fk_part_agency FOREIGN KEY (agency_id) REFERENCES agencies(id)
        );
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_parts_seller ON parts(seller_id);
CREATE INDEX IF NOT EXISTS idx_parts_agency ON parts(agency_id);
CREATE INDEX IF NOT EXISTS idx_parts_category ON parts(category);
CREATE INDEX IF NOT EXISTS idx_parts_status ON parts(status);
CREATE INDEX IF NOT EXISTS idx_parts_brand ON parts(brand);
CREATE INDEX IF NOT EXISTS idx_parts_part_number ON parts(part_number);
