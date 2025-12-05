DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name = 'media' AND column_name = 'vehicle_id') THEN
        ALTER TABLE media ADD COLUMN vehicle_id UUID;
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name = 'media' AND column_name = 'part_id') THEN
        ALTER TABLE media ADD COLUMN part_id UUID;
    END IF;
END $$;
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.table_constraints 
                   WHERE constraint_name = 'fk_media_vehicle') THEN
        ALTER TABLE media ADD CONSTRAINT fk_media_vehicle 
            FOREIGN KEY (vehicle_id) REFERENCES vehicles(id);
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM information_schema.table_constraints 
                   WHERE constraint_name = 'fk_media_part') THEN
        ALTER TABLE media ADD CONSTRAINT fk_media_part 
            FOREIGN KEY (part_id) REFERENCES parts(id);
    END IF;
END $$;
CREATE INDEX IF NOT EXISTS idx_media_vehicle ON media(vehicle_id);
CREATE INDEX IF NOT EXISTS idx_media_part ON media(part_id);
