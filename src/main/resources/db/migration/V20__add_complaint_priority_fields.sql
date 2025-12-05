DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'complaints' AND column_name = 'priority') THEN
        ALTER TABLE complaints ADD COLUMN priority VARCHAR(20) DEFAULT 'MEDIUM';
    END IF;
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'complaints' AND column_name = 'resolved_by_id') THEN
        ALTER TABLE complaints ADD COLUMN resolved_by_id UUID;
    END IF;
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'complaints' AND column_name = 'resolved_at') THEN
        ALTER TABLE complaints ADD COLUMN resolved_at TIMESTAMP;
    END IF;
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'complaints' AND column_name = 'dismissed_by_id') THEN
        ALTER TABLE complaints ADD COLUMN dismissed_by_id UUID;
    END IF;
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'complaints' AND column_name = 'dismissed_at') THEN
        ALTER TABLE complaints ADD COLUMN dismissed_at TIMESTAMP;
    END IF;
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'complaints' AND column_name = 'resolution') THEN
        ALTER TABLE complaints ADD COLUMN resolution TEXT;
    END IF;
END $$;

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'complaints') THEN
        IF NOT EXISTS (SELECT 1 FROM information_schema.table_constraints WHERE constraint_name = 'fk_complaint_resolved_by') THEN
            ALTER TABLE complaints ADD CONSTRAINT fk_complaint_resolved_by FOREIGN KEY (resolved_by_id) REFERENCES users(id);
        END IF;
        IF NOT EXISTS (SELECT 1 FROM information_schema.table_constraints WHERE constraint_name = 'fk_complaint_dismissed_by') THEN
            ALTER TABLE complaints ADD CONSTRAINT fk_complaint_dismissed_by FOREIGN KEY (dismissed_by_id) REFERENCES users(id);
        END IF;
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_complaints_priority ON complaints(priority);
CREATE INDEX IF NOT EXISTS idx_complaints_resolved_by ON complaints(resolved_by_id);
CREATE INDEX IF NOT EXISTS idx_complaints_dismissed_by ON complaints(dismissed_by_id);
