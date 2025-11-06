-- Add OAuth fields to users table
ALTER TABLE users ADD COLUMN google_id VARCHAR(255);
ALTER TABLE users ADD COLUMN apple_id VARCHAR(255);
ALTER TABLE users ADD COLUMN provider VARCHAR(20);
ALTER TABLE users ADD COLUMN image_url VARCHAR(500);

-- Make password_hash nullable (OAuth users don't have passwords)
ALTER TABLE users ALTER COLUMN password_hash DROP NOT NULL;

-- Create unique indexes for OAuth IDs
CREATE UNIQUE INDEX idx_users_google_id ON users(google_id) WHERE google_id IS NOT NULL;
CREATE UNIQUE INDEX idx_users_apple_id ON users(apple_id) WHERE apple_id IS NOT NULL;

-- Create index for provider
CREATE INDEX idx_users_provider ON users(provider);

