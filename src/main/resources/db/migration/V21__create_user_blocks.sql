DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'user_blocks') THEN
        CREATE TABLE user_blocks (
            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
            blocker_id UUID NOT NULL,
            blocked_id UUID NOT NULL,
            reason TEXT,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            CONSTRAINT fk_user_block_blocker FOREIGN KEY (blocker_id) REFERENCES users(id),
            CONSTRAINT fk_user_block_blocked FOREIGN KEY (blocked_id) REFERENCES users(id),
            CONSTRAINT uk_user_block UNIQUE (blocker_id, blocked_id)
        );
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_user_blocks_blocker ON user_blocks(blocker_id);
CREATE INDEX IF NOT EXISTS idx_user_blocks_blocked ON user_blocks(blocked_id);
