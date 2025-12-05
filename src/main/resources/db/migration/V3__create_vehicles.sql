DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'vehicles') THEN
        CREATE TABLE vehicles (
            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
            owner_id UUID NOT NULL,
            agency_id UUID,
            type VARCHAR(50) NOT NULL,
            status VARCHAR(50) NOT NULL,
            brand VARCHAR(100) NOT NULL,
            model VARCHAR(100) NOT NULL,
            year INTEGER NOT NULL,
            color VARCHAR(50),
            license_plate VARCHAR(20),
            vin VARCHAR(17),
            mileage_km INTEGER,
            price DECIMAL(15,2) NOT NULL,
            currency CHAR(3) NOT NULL,
            description TEXT,
            fuel_type VARCHAR(50),
            transmission_type VARCHAR(50),
            engine_size DOUBLE PRECISION,
            horse_power INTEGER,
            number_of_doors INTEGER,
            number_of_seats INTEGER,
            previous_owners INTEGER,
            accident_history BOOLEAN,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            CONSTRAINT fk_vehicle_owner FOREIGN KEY (owner_id) REFERENCES users(id),
            CONSTRAINT fk_vehicle_agency FOREIGN KEY (agency_id) REFERENCES agencies(id)
        );
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_vehicles_owner ON vehicles(owner_id);
CREATE INDEX IF NOT EXISTS idx_vehicles_agency ON vehicles(agency_id);
CREATE INDEX IF NOT EXISTS idx_vehicles_type ON vehicles(type);
CREATE INDEX IF NOT EXISTS idx_vehicles_status ON vehicles(status);
CREATE INDEX IF NOT EXISTS idx_vehicles_brand ON vehicles(brand);
