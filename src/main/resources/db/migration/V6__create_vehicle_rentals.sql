CREATE TABLE vehicle_rentals (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    vehicle_id UUID NOT NULL,
    renter_id UUID NOT NULL,
    agency_id UUID,
    status VARCHAR(50) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    actual_return_date DATE,
    daily_rate DECIMAL(15,2) NOT NULL,
    currency CHAR(3) NOT NULL,
    total_amount DECIMAL(15,2),
    deposit_amount DECIMAL(15,2),
    deposit_returned BOOLEAN,
    notes TEXT,
    pickup_location VARCHAR(255),
    return_location VARCHAR(255),
    mileage_at_pickup INTEGER,
    mileage_at_return INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_rental_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(id),
    CONSTRAINT fk_rental_renter FOREIGN KEY (renter_id) REFERENCES users(id),
    CONSTRAINT fk_rental_agency FOREIGN KEY (agency_id) REFERENCES agencies(id)
);
CREATE INDEX idx_rentals_vehicle ON vehicle_rentals(vehicle_id);
CREATE INDEX idx_rentals_renter ON vehicle_rentals(renter_id);
CREATE INDEX idx_rentals_agency ON vehicle_rentals(agency_id);
CREATE INDEX idx_rentals_status ON vehicle_rentals(status);
CREATE INDEX idx_rentals_dates ON vehicle_rentals(start_date, end_date);
