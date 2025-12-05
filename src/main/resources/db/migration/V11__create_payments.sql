CREATE TABLE payments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    purchase_id UUID NOT NULL,
    payer_id UUID NOT NULL,
    method VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    amount DECIMAL(15,2) NOT NULL,
    currency CHAR(3) NOT NULL,
    transaction_id VARCHAR(255),
    payment_reference VARCHAR(255),
    payment_date TIMESTAMP,
    notes TEXT,
    failure_reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_payment_purchase FOREIGN KEY (purchase_id) REFERENCES purchases(id),
    CONSTRAINT fk_payment_payer FOREIGN KEY (payer_id) REFERENCES users(id)
);
CREATE INDEX idx_payments_purchase ON payments(purchase_id);
CREATE INDEX idx_payments_payer ON payments(payer_id);
CREATE INDEX idx_payments_status ON payments(status);
CREATE INDEX idx_payments_method ON payments(method);
CREATE INDEX idx_payments_transaction_id ON payments(transaction_id);
