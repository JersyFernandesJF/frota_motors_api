-- Add vehicle_id and part_id columns to media table
ALTER TABLE media ADD COLUMN vehicle_id UUID;
ALTER TABLE media ADD COLUMN part_id UUID;

-- Add foreign key constraints
ALTER TABLE media ADD CONSTRAINT fk_media_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(id);
ALTER TABLE media ADD CONSTRAINT fk_media_part FOREIGN KEY (part_id) REFERENCES parts(id);

-- Create indexes
CREATE INDEX idx_media_vehicle ON media(vehicle_id);
CREATE INDEX idx_media_part ON media(part_id);

