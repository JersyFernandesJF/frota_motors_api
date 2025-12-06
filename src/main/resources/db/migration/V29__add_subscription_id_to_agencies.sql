-- Add new columns to agencies table for subscription management
ALTER TABLE agencies ADD COLUMN IF NOT EXISTS subscription_id UUID;
ALTER TABLE agencies ADD COLUMN IF NOT EXISTS is_active BOOLEAN DEFAULT false;
ALTER TABLE agencies ADD COLUMN IF NOT EXISTS current_vehicle_count INTEGER DEFAULT 0;
ALTER TABLE agencies ADD COLUMN IF NOT EXISTS phone VARCHAR(50);
ALTER TABLE agencies ADD COLUMN IF NOT EXISTS address TEXT;
ALTER TABLE agencies ADD COLUMN IF NOT EXISTS tax_id VARCHAR(100);

-- Add foreign key constraint for subscription_id
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.table_constraints 
        WHERE constraint_name = 'fk_agency_subscription'
    ) THEN
        ALTER TABLE agencies 
        ADD CONSTRAINT fk_agency_subscription 
        FOREIGN KEY (subscription_id) REFERENCES subscriptions(id) ON DELETE SET NULL;
    END IF;
END $$;

-- Create index for subscription_id
CREATE INDEX IF NOT EXISTS idx_agencies_subscription ON agencies(subscription_id);
CREATE INDEX IF NOT EXISTS idx_agencies_is_active ON agencies(is_active);