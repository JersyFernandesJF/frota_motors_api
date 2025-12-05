-- Remove property_id column from complaints table
ALTER TABLE complaints DROP COLUMN IF EXISTS reported_property_id;

-- Drop index if it exists
DROP INDEX IF EXISTS idx_complaints_reported_property;


