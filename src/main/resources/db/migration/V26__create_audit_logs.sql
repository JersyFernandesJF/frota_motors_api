DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'audit_logs') THEN
        CREATE TABLE audit_logs (
            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
            user_id UUID,
            action VARCHAR(50) NOT NULL,
            entity_type VARCHAR(100) NOT NULL,
            entity_id UUID,
            ip_address VARCHAR(45),
            user_agent VARCHAR(500),
            request_method VARCHAR(10),
            request_path VARCHAR(500),
            old_values JSONB,
            new_values JSONB,
            status_code INTEGER,
            error_message TEXT,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            CONSTRAINT fk_audit_log_user FOREIGN KEY (user_id) REFERENCES users(id)
        );
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_audit_logs_user ON audit_logs(user_id);
CREATE INDEX IF NOT EXISTS idx_audit_logs_action ON audit_logs(action);
CREATE INDEX IF NOT EXISTS idx_audit_logs_entity_type ON audit_logs(entity_type);
CREATE INDEX IF NOT EXISTS idx_audit_logs_entity_id ON audit_logs(entity_id);
CREATE INDEX IF NOT EXISTS idx_audit_logs_created_at ON audit_logs(created_at);
