DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'agencies') THEN
        CREATE TABLE agencies (
            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
            user_id UUID NOT NULL,
            agency_name VARCHAR(255) NOT NULL,
            license_number VARCHAR(100),
            logo_url VARCHAR(500),
            description TEXT,
            website VARCHAR(255),
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            CONSTRAINT fk_agency_user FOREIGN KEY (user_id) REFERENCES users(id)
        );
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'user_contacts') THEN
        CREATE TABLE user_contacts (
            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
            user_id UUID NOT NULL,
            contact_type VARCHAR(50) NOT NULL CHECK (contact_type IN ('phone', 'mobile', 'whatsapp', 'email_alt', 'linkedin', 'facebook', 'website', 'other')),
            contact_value VARCHAR(255) NOT NULL,
            is_primary BOOLEAN DEFAULT false,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            CONSTRAINT fk_user_contact_user FOREIGN KEY (user_id) REFERENCES users(id)
        );
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_agencies_user ON agencies(user_id);
CREATE INDEX IF NOT EXISTS idx_user_contacts_user ON user_contacts(user_id);
CREATE INDEX IF NOT EXISTS idx_user_contacts_type ON user_contacts(contact_type);
