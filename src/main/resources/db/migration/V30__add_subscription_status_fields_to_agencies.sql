-- Add status and counter fields to agencies for subscription management
ALTER TABLE agencies ADD COLUMN IF NOT EXISTS is_active BOOLEAN DEFAULT false;
ALTER TABLE agencies ADD COLUMN IF NOT EXISTS current_vehicle_count INTEGER DEFAULT 0;

-- Create indexes for subscription management
CREATE INDEX IF NOT EXISTS idx_agencies_subscription ON agencies(subscription_id);
CREATE INDEX IF NOT EXISTS idx_agencies_is_active ON agencies(is_active);

