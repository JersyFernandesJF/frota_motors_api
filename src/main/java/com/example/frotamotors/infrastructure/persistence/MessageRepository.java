package com.example.frotamotors.infrastructure.persistence;

import com.example.frotamotors.domain.model.Message;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
  List<Message> findByConversationIdOrderByCreatedAtAsc(UUID conversationId);

  Page<Message> findByConversationIdOrderByCreatedAtDesc(UUID conversationId, Pageable pageable);

  @Query(
      "SELECT COUNT(m) FROM Message m WHERE "
          + "m.conversation.id = :conversationId AND "
          + "m.sender.id != :userId AND "
          + "m.isRead = false")
  Long countUnreadMessages(
      @Param("conversationId") UUID conversationId, @Param("userId") UUID userId);
}
