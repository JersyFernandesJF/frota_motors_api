-- Add company information fields to agencies
ALTER TABLE agencies ADD COLUMN IF NOT EXISTS phone VARCHAR(50);
ALTER TABLE agencies ADD COLUMN IF NOT EXISTS address TEXT;
ALTER TABLE agencies ADD COLUMN IF NOT EXISTS tax_id VARCHAR(100);

