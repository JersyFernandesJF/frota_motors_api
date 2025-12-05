CREATE TABLE media (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    vehicle_id UUID,
    part_id UUID,
    media_type VARCHAR(50) NOT NULL CHECK (media_type IN ('photo', 'video', 'floorplan', 'virtual_tour')),
    url VARCHAR(500) NOT NULL,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_media_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(id),
    CONSTRAINT fk_media_part FOREIGN KEY (part_id) REFERENCES parts(id),
    CONSTRAINT chk_media_reference CHECK (
        (vehicle_id IS NOT NULL AND part_id IS NULL) OR 
        (vehicle_id IS NULL AND part_id IS NOT NULL)
    )
);
CREATE TABLE favorites (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    vehicle_id UUID,
    part_id UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_favorite_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_favorite_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(id),
    CONSTRAINT fk_favorite_part FOREIGN KEY (part_id) REFERENCES parts(id),
    CONSTRAINT chk_favorite_reference CHECK (
        (vehicle_id IS NOT NULL AND part_id IS NULL) OR 
        (vehicle_id IS NULL AND part_id IS NOT NULL)
    )
);
CREATE INDEX idx_media_vehicle ON media(vehicle_id);
CREATE INDEX idx_media_part ON media(part_id);
CREATE INDEX idx_media_type ON media(media_type);
CREATE INDEX idx_favorites_user ON favorites(user_id);
CREATE INDEX idx_favorites_vehicle ON favorites(vehicle_id);
CREATE INDEX idx_favorites_part ON favorites(part_id);
CREATE UNIQUE INDEX unique_user_vehicle ON favorites(user_id, vehicle_id) WHERE vehicle_id IS NOT NULL;
CREATE UNIQUE INDEX unique_user_part ON favorites(user_id, part_id) WHERE part_id IS NOT NULL;
