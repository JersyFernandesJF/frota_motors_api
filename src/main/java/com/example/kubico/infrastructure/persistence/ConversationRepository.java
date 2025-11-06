package com.example.kubico.infrastructure.persistence;

import com.example.kubico.domain.model.Conversation;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, UUID> {
  @Query(
      "SELECT c FROM Conversation c WHERE "
          + "(c.user1.id = :userId1 AND c.user2.id = :userId2) OR "
          + "(c.user1.id = :userId2 AND c.user2.id = :userId1)")
  Optional<Conversation> findConversationBetweenUsers(
      @Param("userId1") UUID userId1, @Param("userId2") UUID userId2);

  @Query(
      "SELECT c FROM Conversation c WHERE "
          + "c.user1.id = :userId OR c.user2.id = :userId "
          + "ORDER BY c.lastMessageAt DESC NULLS LAST, c.updatedAt DESC")
  List<Conversation> findByUserId(@Param("userId") UUID userId);

  @Query(
      "SELECT c FROM Conversation c WHERE "
          + "c.user1.id = :userId OR c.user2.id = :userId "
          + "ORDER BY c.lastMessageAt DESC NULLS LAST, c.updatedAt DESC")
  Page<Conversation> findByUserId(@Param("userId") UUID userId, Pageable pageable);
}

