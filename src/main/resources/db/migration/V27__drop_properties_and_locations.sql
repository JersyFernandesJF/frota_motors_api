-- Drop constraints from media table
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'media') THEN
        ALTER TABLE media DROP CONSTRAINT IF EXISTS fk_media_property;
        ALTER TABLE media DROP CONSTRAINT IF EXISTS media_property_id_fkey;
        ALTER TABLE media DROP COLUMN IF EXISTS property_id;
    END IF;
END $$;

-- Drop constraints from favorites table
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'favorites') THEN
        ALTER TABLE favorites DROP CONSTRAINT IF EXISTS fk_favorite_property;
        ALTER TABLE favorites DROP CONSTRAINT IF EXISTS favorites_property_id_fkey;
        ALTER TABLE favorites DROP COLUMN IF EXISTS property_id;
    END IF;
END $$;

-- Drop constraints from complaints table
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'complaints') THEN
        ALTER TABLE complaints DROP CONSTRAINT IF EXISTS fk_complaint_reported_property;
        ALTER TABLE complaints DROP CONSTRAINT IF EXISTS complaints_reported_property_id_fkey;
        ALTER TABLE complaints DROP COLUMN IF EXISTS reported_property_id;
        DROP INDEX IF EXISTS idx_complaints_reported_property;
    END IF;
END $$;

-- Drop constraints from shared_list_items table (if exists)
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'shared_list_items') THEN
        ALTER TABLE shared_list_items DROP CONSTRAINT IF EXISTS fk_shared_list_item_property;
        ALTER TABLE shared_list_items DROP CONSTRAINT IF EXISTS shared_list_items_property_id_fkey;
        ALTER TABLE shared_list_items DROP CONSTRAINT IF EXISTS shared_list_items_list_id_fkey;
    END IF;
END $$;

-- Drop constraints from shared_list_members table (if exists)
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'shared_list_members') THEN
        ALTER TABLE shared_list_members DROP CONSTRAINT IF EXISTS shared_list_members_list_id_fkey;
        ALTER TABLE shared_list_members DROP CONSTRAINT IF EXISTS shared_list_members_user_id_fkey;
    END IF;
END $$;

-- Drop constraints from shared_lists table (if exists)
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'shared_lists') THEN
        ALTER TABLE shared_lists DROP CONSTRAINT IF EXISTS shared_lists_owner_id_fkey;
    END IF;
END $$;

-- Drop constraints from locations table (if exists)
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'locations') THEN
        ALTER TABLE locations DROP CONSTRAINT IF EXISTS locations_property_id_fkey;
        ALTER TABLE locations DROP CONSTRAINT IF EXISTS fk_location_property;
        DROP INDEX IF EXISTS idx_locations_property;
    END IF;
END $$;

-- Drop constraints from properties table (if exists)
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'properties') THEN
        ALTER TABLE properties DROP CONSTRAINT IF EXISTS properties_owner_id_fkey;
        ALTER TABLE properties DROP CONSTRAINT IF EXISTS fk_property_owner;
        ALTER TABLE properties DROP CONSTRAINT IF EXISTS properties_agency_id_fkey;
        ALTER TABLE properties DROP CONSTRAINT IF EXISTS fk_property_agency;
        DROP INDEX IF EXISTS unique_user_property;
        DROP INDEX IF EXISTS idx_properties_owner;
        DROP INDEX IF EXISTS idx_properties_agency;
        DROP INDEX IF EXISTS idx_properties_type;
        DROP INDEX IF EXISTS idx_properties_status;
    END IF;
END $$;

-- Drop tables if they exist
DROP TABLE IF EXISTS shared_list_items CASCADE;
DROP TABLE IF EXISTS shared_list_members CASCADE;
DROP TABLE IF EXISTS shared_lists CASCADE;
DROP TABLE IF EXISTS locations CASCADE;
DROP TABLE IF EXISTS properties CASCADE;
