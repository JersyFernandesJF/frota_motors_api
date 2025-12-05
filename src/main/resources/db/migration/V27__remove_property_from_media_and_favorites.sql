-- Remove property_id column from media table
ALTER TABLE media DROP COLUMN IF EXISTS property_id;

-- Remove property_id column from favorites table
ALTER TABLE favorites DROP COLUMN IF EXISTS property_id;

-- Drop unique index for user and property
DROP INDEX IF EXISTS unique_user_property;


