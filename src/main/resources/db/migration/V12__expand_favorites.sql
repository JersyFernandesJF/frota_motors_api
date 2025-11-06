-- Add vehicle_id and part_id columns to favorites table
ALTER TABLE favorites ADD COLUMN vehicle_id UUID;
ALTER TABLE favorites ADD COLUMN part_id UUID;

-- Make property_id nullable since now we can favorite vehicles and parts too
ALTER TABLE favorites ALTER COLUMN property_id DROP NOT NULL;

-- Add foreign key constraints
ALTER TABLE favorites ADD CONSTRAINT fk_favorite_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(id);
ALTER TABLE favorites ADD CONSTRAINT fk_favorite_part FOREIGN KEY (part_id) REFERENCES parts(id);

-- Create indexes
CREATE INDEX idx_favorites_vehicle ON favorites(vehicle_id);
CREATE INDEX idx_favorites_part ON favorites(part_id);

-- Remove unique constraint on user_id and property_id, add new unique constraints
ALTER TABLE favorites DROP CONSTRAINT IF EXISTS unique_user_property;
CREATE UNIQUE INDEX unique_user_property ON favorites(user_id, property_id) WHERE property_id IS NOT NULL;
CREATE UNIQUE INDEX unique_user_vehicle ON favorites(user_id, vehicle_id) WHERE vehicle_id IS NOT NULL;
CREATE UNIQUE INDEX unique_user_part ON favorites(user_id, part_id) WHERE part_id IS NOT NULL;

