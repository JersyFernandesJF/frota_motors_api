CREATE TABLE complaints (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    reporter_id UUID NOT NULL,
    type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    description TEXT NOT NULL,
    reported_user_id UUID,
    reported_vehicle_id UUID,
    reported_part_id UUID,
    reported_agency_id UUID,
    reviewed_by_id UUID,
    admin_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_complaint_reporter FOREIGN KEY (reporter_id) REFERENCES users(id),
    CONSTRAINT fk_complaint_reported_user FOREIGN KEY (reported_user_id) REFERENCES users(id),
    CONSTRAINT fk_complaint_reported_vehicle FOREIGN KEY (reported_vehicle_id) REFERENCES vehicles(id),
    CONSTRAINT fk_complaint_reported_part FOREIGN KEY (reported_part_id) REFERENCES parts(id),
    CONSTRAINT fk_complaint_reported_agency FOREIGN KEY (reported_agency_id) REFERENCES agencies(id),
    CONSTRAINT fk_complaint_reviewed_by FOREIGN KEY (reviewed_by_id) REFERENCES users(id)
);
CREATE INDEX idx_complaints_reporter ON complaints(reporter_id);
CREATE INDEX idx_complaints_status ON complaints(status);
CREATE INDEX idx_complaints_type ON complaints(type);
CREATE INDEX idx_complaints_reported_user ON complaints(reported_user_id);
CREATE INDEX idx_complaints_reported_vehicle ON complaints(reported_vehicle_id);
CREATE INDEX idx_complaints_reported_part ON complaints(reported_part_id);
CREATE INDEX idx_complaints_reported_agency ON complaints(reported_agency_id);
