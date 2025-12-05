DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'financings') THEN
        CREATE TABLE financings (
            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
            vehicle_id UUID NOT NULL,
            buyer_id UUID NOT NULL,
            seller_id UUID NOT NULL,
            status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
            vehicle_price DECIMAL(15,2) NOT NULL,
            down_payment DECIMAL(15,2) NOT NULL,
            financing_amount DECIMAL(15,2) NOT NULL,
            interest_rate DECIMAL(5,2) NOT NULL,
            loan_term_months INTEGER NOT NULL,
            monthly_payment DECIMAL(15,2) NOT NULL,
            credit_score INTEGER,
            credit_score_simulation INTEGER,
            rejection_reason TEXT,
            approved_by_id UUID,
            approved_at TIMESTAMP,
            rejected_by_id UUID,
            rejected_at TIMESTAMP,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            CONSTRAINT fk_financing_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(id),
            CONSTRAINT fk_financing_buyer FOREIGN KEY (buyer_id) REFERENCES users(id),
            CONSTRAINT fk_financing_seller FOREIGN KEY (seller_id) REFERENCES users(id),
            CONSTRAINT fk_financing_approved_by FOREIGN KEY (approved_by_id) REFERENCES users(id),
            CONSTRAINT fk_financing_rejected_by FOREIGN KEY (rejected_by_id) REFERENCES users(id)
        );
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_financings_vehicle ON financings(vehicle_id);
CREATE INDEX IF NOT EXISTS idx_financings_buyer ON financings(buyer_id);
CREATE INDEX IF NOT EXISTS idx_financings_seller ON financings(seller_id);
CREATE INDEX IF NOT EXISTS idx_financings_status ON financings(status);
CREATE INDEX IF NOT EXISTS idx_financings_approved_by ON financings(approved_by_id);
CREATE INDEX IF NOT EXISTS idx_financings_rejected_by ON financings(rejected_by_id);
