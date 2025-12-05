DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'user_activities') THEN
        CREATE TABLE user_activities (
            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
            user_id UUID NOT NULL,
            type VARCHAR(50) NOT NULL,
            description TEXT NOT NULL,
            related_entity_type VARCHAR(50),
            related_entity_id UUID,
            metadata JSONB,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            CONSTRAINT fk_user_activity_user FOREIGN KEY (user_id) REFERENCES users(id)
        );
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_user_activities_user ON user_activities(user_id);
CREATE INDEX IF NOT EXISTS idx_user_activities_type ON user_activities(type);
CREATE INDEX IF NOT EXISTS idx_user_activities_created_at ON user_activities(created_at);
