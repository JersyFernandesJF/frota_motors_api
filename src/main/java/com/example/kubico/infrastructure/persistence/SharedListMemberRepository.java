package com.example.kubico.infrastructure.persistence;

import com.example.kubico.domain.model.SharedListMember;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SharedListMemberRepository extends JpaRepository<SharedListMember, UUID> {}
