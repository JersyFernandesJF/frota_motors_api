DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'vehicles' AND column_name = 'moderation_status') THEN
        ALTER TABLE vehicles ADD COLUMN moderation_status VARCHAR(20) DEFAULT 'PENDING';
    END IF;
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'vehicles' AND column_name = 'approved_by_id') THEN
        ALTER TABLE vehicles ADD COLUMN approved_by_id UUID;
    END IF;
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'vehicles' AND column_name = 'approved_at') THEN
        ALTER TABLE vehicles ADD COLUMN approved_at TIMESTAMP;
    END IF;
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'vehicles' AND column_name = 'rejected_by_id') THEN
        ALTER TABLE vehicles ADD COLUMN rejected_by_id UUID;
    END IF;
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'vehicles' AND column_name = 'rejected_at') THEN
        ALTER TABLE vehicles ADD COLUMN rejected_at TIMESTAMP;
    END IF;
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'vehicles' AND column_name = 'rejection_reason') THEN
        ALTER TABLE vehicles ADD COLUMN rejection_reason TEXT;
    END IF;
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'vehicles' AND column_name = 'published_at') THEN
        ALTER TABLE vehicles ADD COLUMN published_at TIMESTAMP;
    END IF;
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'vehicles' AND column_name = 'views') THEN
        ALTER TABLE vehicles ADD COLUMN views BIGINT DEFAULT 0;
    END IF;
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'vehicles' AND column_name = 'favorites_count') THEN
        ALTER TABLE vehicles ADD COLUMN favorites_count BIGINT DEFAULT 0;
    END IF;
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'vehicles' AND column_name = 'messages_count') THEN
        ALTER TABLE vehicles ADD COLUMN messages_count BIGINT DEFAULT 0;
    END IF;
END $$;

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'vehicles') THEN
        IF NOT EXISTS (SELECT 1 FROM information_schema.table_constraints WHERE constraint_name = 'fk_vehicle_approved_by') THEN
            ALTER TABLE vehicles ADD CONSTRAINT fk_vehicle_approved_by FOREIGN KEY (approved_by_id) REFERENCES users(id);
        END IF;
        IF NOT EXISTS (SELECT 1 FROM information_schema.table_constraints WHERE constraint_name = 'fk_vehicle_rejected_by') THEN
            ALTER TABLE vehicles ADD CONSTRAINT fk_vehicle_rejected_by FOREIGN KEY (rejected_by_id) REFERENCES users(id);
        END IF;
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_vehicles_moderation_status ON vehicles(moderation_status);
CREATE INDEX IF NOT EXISTS idx_vehicles_approved_by ON vehicles(approved_by_id);
CREATE INDEX IF NOT EXISTS idx_vehicles_rejected_by ON vehicles(rejected_by_id);
CREATE INDEX IF NOT EXISTS idx_vehicles_published_at ON vehicles(published_at);
