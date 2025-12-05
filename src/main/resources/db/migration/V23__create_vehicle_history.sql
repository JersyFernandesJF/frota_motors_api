CREATE TABLE vehicle_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    vehicle_id UUID NOT NULL,
    action VARCHAR(50) NOT NULL,
    old_value TEXT,
    new_value TEXT,
    changed_by_id UUID,
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_vehicle_history_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(id),
    CONSTRAINT fk_vehicle_history_changed_by FOREIGN KEY (changed_by_id) REFERENCES users(id)
);
CREATE INDEX idx_vehicle_history_vehicle ON vehicle_history(vehicle_id);
CREATE INDEX idx_vehicle_history_changed_at ON vehicle_history(changed_at);
CREATE INDEX idx_vehicle_history_action ON vehicle_history(action);
