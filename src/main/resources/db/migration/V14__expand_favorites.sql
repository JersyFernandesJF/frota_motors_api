DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name = 'favorites' AND column_name = 'vehicle_id') THEN
        ALTER TABLE favorites ADD COLUMN vehicle_id UUID;
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name = 'favorites' AND column_name = 'part_id') THEN
        ALTER TABLE favorites ADD COLUMN part_id UUID;
    END IF;
END $$;
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.table_constraints 
                   WHERE constraint_name = 'fk_favorite_vehicle') THEN
        ALTER TABLE favorites ADD CONSTRAINT fk_favorite_vehicle 
            FOREIGN KEY (vehicle_id) REFERENCES vehicles(id);
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM information_schema.table_constraints 
                   WHERE constraint_name = 'fk_favorite_part') THEN
        ALTER TABLE favorites ADD CONSTRAINT fk_favorite_part 
            FOREIGN KEY (part_id) REFERENCES parts(id);
    END IF;
END $$;
CREATE INDEX IF NOT EXISTS idx_favorites_vehicle ON favorites(vehicle_id);
CREATE INDEX IF NOT EXISTS idx_favorites_part ON favorites(part_id);
CREATE UNIQUE INDEX IF NOT EXISTS unique_user_vehicle ON favorites(user_id, vehicle_id) WHERE vehicle_id IS NOT NULL;
CREATE UNIQUE INDEX IF NOT EXISTS unique_user_part ON favorites(user_id, part_id) WHERE part_id IS NOT NULL;
