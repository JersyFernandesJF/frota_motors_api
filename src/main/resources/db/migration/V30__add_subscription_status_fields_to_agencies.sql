-- Add status and counter fields to agencies for subscription management
ALTER TABLE agencies ADD COLUMN IF NOT EXISTS is_active BOOLEAN DEFAULT false;
ALTER TABLE agencies ADD COLUMN IF NOT EXISTS current_vehicle_count INTEGER DEFAULT 0;

-- Create index for is_active field
CREATE INDEX IF NOT EXISTS idx_agencies_is_active ON agencies(is_active);

