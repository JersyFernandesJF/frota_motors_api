DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns 
               WHERE table_name = 'complaints' AND column_name = 'reported_property_id') THEN
        ALTER TABLE complaints DROP COLUMN reported_property_id;
    END IF;
END $$;
DROP INDEX IF EXISTS idx_complaints_reported_property;
