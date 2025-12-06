-- Add subscription_id column to agencies table
ALTER TABLE agencies ADD COLUMN IF NOT EXISTS subscription_id UUID;

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

