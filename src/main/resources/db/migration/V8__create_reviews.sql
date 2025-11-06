CREATE TABLE reviews (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    reviewer_id UUID NOT NULL,
    type VARCHAR(50) NOT NULL,
    reviewed_user_id UUID,
    reviewed_vehicle_id UUID,
    reviewed_part_id UUID,
    reviewed_agency_id UUID,
    reviewed_rental_id UUID,
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_review_reviewer FOREIGN KEY (reviewer_id) REFERENCES users(id),
    CONSTRAINT fk_review_reviewed_user FOREIGN KEY (reviewed_user_id) REFERENCES users(id),
    CONSTRAINT fk_review_reviewed_vehicle FOREIGN KEY (reviewed_vehicle_id) REFERENCES vehicles(id),
    CONSTRAINT fk_review_reviewed_part FOREIGN KEY (reviewed_part_id) REFERENCES parts(id),
    CONSTRAINT fk_review_reviewed_agency FOREIGN KEY (reviewed_agency_id) REFERENCES agencies(id),
    CONSTRAINT fk_review_reviewed_rental FOREIGN KEY (reviewed_rental_id) REFERENCES vehicle_rentals(id)
);

CREATE INDEX idx_reviews_reviewer ON reviews(reviewer_id);
CREATE INDEX idx_reviews_type ON reviews(type);
CREATE INDEX idx_reviews_reviewed_user ON reviews(reviewed_user_id);
CREATE INDEX idx_reviews_reviewed_vehicle ON reviews(reviewed_vehicle_id);
CREATE INDEX idx_reviews_reviewed_part ON reviews(reviewed_part_id);
CREATE INDEX idx_reviews_reviewed_agency ON reviews(reviewed_agency_id);
CREATE INDEX idx_reviews_reviewed_rental ON reviews(reviewed_rental_id);
CREATE INDEX idx_reviews_rating ON reviews(rating);

