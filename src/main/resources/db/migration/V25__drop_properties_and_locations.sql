-- Drop foreign key constraints that reference properties and locations
-- Note: Some constraints may have different names depending on database version
ALTER TABLE media DROP CONSTRAINT IF EXISTS fk_media_property;
ALTER TABLE favorites DROP CONSTRAINT IF EXISTS fk_favorite_property;
ALTER TABLE complaints DROP CONSTRAINT IF EXISTS fk_complaint_reported_property;
ALTER TABLE shared_list_items DROP CONSTRAINT IF EXISTS fk_shared_list_item_property;
ALTER TABLE shared_list_items DROP CONSTRAINT IF EXISTS shared_list_items_list_id_fkey;
ALTER TABLE shared_list_items DROP CONSTRAINT IF EXISTS shared_list_items_property_id_fkey;
ALTER TABLE shared_list_members DROP CONSTRAINT IF EXISTS shared_list_members_list_id_fkey;
ALTER TABLE shared_list_members DROP CONSTRAINT IF EXISTS shared_list_members_user_id_fkey;
ALTER TABLE shared_lists DROP CONSTRAINT IF EXISTS shared_lists_owner_id_fkey;
ALTER TABLE locations DROP CONSTRAINT IF EXISTS locations_property_id_fkey;
ALTER TABLE properties DROP CONSTRAINT IF EXISTS properties_owner_id_fkey;
ALTER TABLE properties DROP CONSTRAINT IF EXISTS properties_agency_id_fkey;

-- Drop indexes related to properties
DROP INDEX IF EXISTS idx_complaints_reported_property;
DROP INDEX IF EXISTS unique_user_property;

-- Drop tables (order matters due to foreign keys)
DROP TABLE IF EXISTS shared_list_items;
DROP TABLE IF EXISTS shared_list_members;
DROP TABLE IF EXISTS shared_lists;
DROP TABLE IF EXISTS locations;
DROP TABLE IF EXISTS properties;

