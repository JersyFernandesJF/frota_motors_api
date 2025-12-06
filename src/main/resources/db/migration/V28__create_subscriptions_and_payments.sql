-- Create subscriptions table
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'subscriptions') THEN
        CREATE TABLE subscriptions (
            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
            agency_id UUID NOT NULL,
            plan_type VARCHAR(50) NOT NULL CHECK (plan_type IN ('BASIC', 'PREMIUM', 'ENTERPRISE')),
            monthly_price DECIMAL(10,2) NOT NULL,
            currency CHAR(3) NOT NULL CHECK (currency IN ('USD', 'EUR')),
            max_vehicles INTEGER NOT NULL,
            status VARCHAR(20) NOT NULL CHECK (status IN ('ACTIVE', 'SUSPENDED', 'CANCELLED', 'EXPIRED', 'PENDING')),
            start_date TIMESTAMP NOT NULL,
            end_date TIMESTAMP,
            next_billing_date TIMESTAMP,
            auto_renew BOOLEAN DEFAULT true,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            CONSTRAINT fk_subscription_agency FOREIGN KEY (agency_id) REFERENCES agencies(id) ON DELETE CASCADE
        );
        
        CREATE INDEX IF NOT EXISTS idx_subscriptions_agency ON subscriptions(agency_id);
        CREATE INDEX IF NOT EXISTS idx_subscriptions_status ON subscriptions(status);
        CREATE INDEX IF NOT EXISTS idx_subscriptions_next_billing ON subscriptions(next_billing_date);
    END IF;
END $$;

-- Create subscription_payments table
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'subscription_payments') THEN
        CREATE TABLE subscription_payments (
            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
            subscription_id UUID NOT NULL,
            amount DECIMAL(10,2) NOT NULL,
            currency CHAR(3) NOT NULL CHECK (currency IN ('USD', 'EUR')),
            payment_method VARCHAR(50) NOT NULL,
            payment_status VARCHAR(20) NOT NULL CHECK (payment_status IN ('PENDING', 'COMPLETED', 'FAILED', 'REFUNDED')),
            transaction_id VARCHAR(255),
            payment_date TIMESTAMP,
            due_date TIMESTAMP NOT NULL,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            CONSTRAINT fk_payment_subscription FOREIGN KEY (subscription_id) REFERENCES subscriptions(id) ON DELETE CASCADE
        );
        
        CREATE INDEX IF NOT EXISTS idx_subscription_payments_subscription ON subscription_payments(subscription_id);
        CREATE INDEX IF NOT EXISTS idx_subscription_payments_status ON subscription_payments(payment_status);
        CREATE INDEX IF NOT EXISTS idx_subscription_payments_due_date ON subscription_payments(due_date);
    END IF;
END $$;

