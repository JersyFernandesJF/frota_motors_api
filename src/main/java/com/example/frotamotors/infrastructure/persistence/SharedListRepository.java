package com.example.frotamotors.infrastructure.persistence;

import com.example.frotamotors.domain.model.SharedList;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SharedListRepository extends JpaRepository<SharedList, UUID> {}
