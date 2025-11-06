package com.example.kubico.domain.service;

import com.example.kubico.domain.model.Conversation;
import com.example.kubico.domain.model.Message;
import com.example.kubico.domain.model.User;
import com.example.kubico.infrastructure.dto.MessageCreateDTO;
import com.example.kubico.infrastructure.mapper.MessageMapper;
import com.example.kubico.infrastructure.persistence.ConversationRepository;
import com.example.kubico.infrastructure.persistence.MessageRepository;
import com.example.kubico.infrastructure.persistence.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageService {

  @Autowired private MessageRepository messageRepository;

  @Autowired private ConversationRepository conversationRepository;

  @Autowired private UserRepository userRepository;

  @Transactional
  public Message sendMessage(MessageCreateDTO dto) {
    Conversation conversation =
        conversationRepository
            .findById(dto.conversationId())
            .orElseThrow(() -> new EntityNotFoundException("Conversation not found"));

    User sender =
        userRepository
            .findById(dto.senderId())
            .orElseThrow(() -> new EntityNotFoundException("Sender not found"));

    // Verify sender is part of the conversation
    if (!conversation.getUser1().getId().equals(sender.getId())
        && !conversation.getUser2().getId().equals(sender.getId())) {
      throw new IllegalStateException("Sender is not part of this conversation");
    }

    Message message = MessageMapper.toEntity(dto, conversation, sender);
    Message saved = messageRepository.save(message);

    // Update conversation last message time
    conversation.setLastMessageAt(LocalDateTime.now());
    conversationRepository.save(conversation);

    return saved;
  }

  @Transactional
  public Conversation getOrCreateConversation(UUID user1Id, UUID user2Id) {
    if (user1Id.equals(user2Id)) {
      throw new IllegalArgumentException("Cannot create conversation with yourself");
    }

    Optional<Conversation> existing =
        conversationRepository.findConversationBetweenUsers(user1Id, user2Id);

    if (existing.isPresent()) {
      return existing.get();
    }

    User user1 =
        userRepository
            .findById(user1Id)
            .orElseThrow(() -> new EntityNotFoundException("User 1 not found"));
    User user2 =
        userRepository
            .findById(user2Id)
            .orElseThrow(() -> new EntityNotFoundException("User 2 not found"));

    Conversation conversation = new Conversation();
    conversation.setUser1(user1);
    conversation.setUser2(user2);
    return conversationRepository.save(conversation);
  }

  public List<Message> getMessagesByConversation(UUID conversationId) {
    return messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
  }

  public Page<Message> getMessagesByConversation(UUID conversationId, Pageable pageable) {
    return messageRepository.findByConversationIdOrderByCreatedAtDesc(conversationId, pageable);
  }

  public List<Conversation> getConversationsByUser(UUID userId) {
    return conversationRepository.findByUserId(userId);
  }

  public Page<Conversation> getConversationsByUser(UUID userId, Pageable pageable) {
    return conversationRepository.findByUserId(userId, pageable);
  }

  @Transactional
  public void markMessagesAsRead(UUID conversationId, UUID userId) {
    List<Message> messages = messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
    messages.stream()
        .filter(m -> !m.getSender().getId().equals(userId) && !m.getIsRead())
        .forEach(m -> m.setIsRead(true));
    messageRepository.saveAll(messages);
  }

  public Long getUnreadMessageCount(UUID conversationId, UUID userId) {
    return messageRepository.countUnreadMessages(conversationId, userId);
  }

  public Message getById(UUID id) {
    return messageRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Message not found"));
  }
}

