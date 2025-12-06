package com.example.frotamotors.domain.service;

import com.example.frotamotors.domain.model.Conversation;
import com.example.frotamotors.domain.model.Message;
import com.example.frotamotors.domain.model.User;
import com.example.frotamotors.domain.model.UserBlock;
import com.example.frotamotors.infrastructure.dto.ExportRequestDTO;
import com.example.frotamotors.infrastructure.dto.MessageCreateDTO;
import com.example.frotamotors.infrastructure.dto.UserWarnRequestDTO;
import com.example.frotamotors.infrastructure.mapper.MessageMapper;
import com.example.frotamotors.infrastructure.persistence.ComplaintRepository;
import com.example.frotamotors.infrastructure.persistence.ConversationRepository;
import com.example.frotamotors.infrastructure.persistence.MessageRepository;
import com.example.frotamotors.infrastructure.persistence.UserBlockRepository;
import com.example.frotamotors.infrastructure.persistence.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class MessageService {

  @Autowired private MessageRepository messageRepository;

  @Autowired private ConversationRepository conversationRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private UserBlockRepository userBlockRepository;

  @Autowired private ComplaintRepository complaintRepository;

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

    // Check if user is blocked
    User otherUser =
        conversation.getUser1().getId().equals(sender.getId())
            ? conversation.getUser2()
            : conversation.getUser1();

    if (userBlockRepository.existsByBlockerIdAndBlockedId(otherUser.getId(), sender.getId())) {
      throw new IllegalStateException("You are blocked by this user");
    }

    if (userBlockRepository.existsByBlockerIdAndBlockedId(sender.getId(), otherUser.getId())) {
      throw new IllegalStateException("You have blocked this user");
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

  public Conversation getConversationById(UUID conversationId) {
    return conversationRepository
        .findById(conversationId)
        .orElse(null);
  }

  public Page<Conversation> getConversationsByUser(UUID userId, Pageable pageable) {
    return conversationRepository.findByUserId(userId, pageable);
  }

  @Transactional
  public void markMessagesAsRead(UUID conversationId, UUID userId) {
    List<Message> messages =
        messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
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

  @Transactional
  public UserBlock blockUser(UUID blockerId, UUID blockedId, String reason) {
    if (blockerId.equals(blockedId)) {
      throw new IllegalArgumentException("Cannot block yourself");
    }

    User blocker =
        userRepository
            .findById(blockerId)
            .orElseThrow(() -> new EntityNotFoundException("Blocker not found"));
    User blocked =
        userRepository
            .findById(blockedId)
            .orElseThrow(() -> new EntityNotFoundException("Blocked user not found"));

    Optional<UserBlock> existing =
        userBlockRepository.findByBlockerIdAndBlockedId(blockerId, blockedId);
    if (existing.isPresent()) {
      throw new IllegalStateException("User is already blocked");
    }

    UserBlock block = new UserBlock();
    block.setBlocker(blocker);
    block.setBlocked(blocked);
    block.setReason(reason);
    return userBlockRepository.save(block);
  }

  @Transactional
  public void unblockUser(UUID blockerId, UUID blockedId) {
    UserBlock block =
        userBlockRepository
            .findByBlockerIdAndBlockedId(blockerId, blockedId)
            .orElseThrow(() -> new EntityNotFoundException("Block not found"));
    userBlockRepository.delete(block);
  }

  public boolean isBlocked(UUID blockerId, UUID blockedId) {
    return userBlockRepository.existsByBlockerIdAndBlockedId(blockerId, blockedId);
  }

  public Page<Conversation> getAllConversations(Pageable pageable) {
    return conversationRepository.findAll(pageable);
  }

  public Page<Conversation> getAllConversations(
      String search, Boolean isReported, Boolean isBlocked, Pageable pageable) {
    // If no filters are applied, use the simple method
    if ((search == null || search.trim().isEmpty()) 
        && isReported == null 
        && isBlocked == null) {
      return getAllConversations(pageable);
    }
    
    // Get all conversations (without pagination) to apply filters
    // TODO: Optimize this with a custom query in ConversationRepository for better performance
    List<Conversation> allConversations = conversationRepository.findAll();
    
    // Apply filters
    List<Conversation> filtered = allConversations.stream()
        .filter(conversation -> {
          // Filter by isReported: check if there are complaints about either user in the conversation
          if (isReported != null && isReported) {
            UUID user1Id = conversation.getUser1() != null ? conversation.getUser1().getId() : null;
            UUID user2Id = conversation.getUser2() != null ? conversation.getUser2().getId() : null;
            
            boolean hasComplaints = false;
            if (user1Id != null) {
              hasComplaints = complaintRepository.findByReportedUserId(user1Id).stream()
                  .anyMatch(c -> c.getStatus() == com.example.frotamotors.domain.enums.ComplaintStatus.PENDING);
            }
            if (!hasComplaints && user2Id != null) {
              hasComplaints = complaintRepository.findByReportedUserId(user2Id).stream()
                  .anyMatch(c -> c.getStatus() == com.example.frotamotors.domain.enums.ComplaintStatus.PENDING);
            }
            if (!hasComplaints) {
              return false;
            }
          }
          
          // Filter by isBlocked: check if there is a block between the two users
          if (isBlocked != null && isBlocked) {
            UUID user1Id = conversation.getUser1() != null ? conversation.getUser1().getId() : null;
            UUID user2Id = conversation.getUser2() != null ? conversation.getUser2().getId() : null;
            
            if (user1Id != null && user2Id != null) {
              boolean hasBlock = userBlockRepository.existsByBlockerIdAndBlockedId(user1Id, user2Id)
                  || userBlockRepository.existsByBlockerIdAndBlockedId(user2Id, user1Id);
              if (!hasBlock) {
                return false;
              }
            } else {
              return false;
            }
          }
          
          // Filter by search: search in user names and emails
          if (search != null && !search.trim().isEmpty()) {
            String searchLower = search.toLowerCase();
            boolean matches = false;
            
            if (conversation.getUser1() != null) {
              matches = (conversation.getUser1().getName() != null 
                      && conversation.getUser1().getName().toLowerCase().contains(searchLower))
                  || (conversation.getUser1().getEmail() != null 
                      && conversation.getUser1().getEmail().toLowerCase().contains(searchLower));
            }
            if (!matches && conversation.getUser2() != null) {
              matches = (conversation.getUser2().getName() != null 
                      && conversation.getUser2().getName().toLowerCase().contains(searchLower))
                  || (conversation.getUser2().getEmail() != null 
                      && conversation.getUser2().getEmail().toLowerCase().contains(searchLower));
            }
            
            if (!matches) {
              return false;
            }
          }
          
          return true;
        })
        .collect(java.util.stream.Collectors.toList());
    
    // Convert back to Page
    int start = (int) pageable.getOffset();
    int end = Math.min((start + pageable.getPageSize()), filtered.size());
    List<Conversation> pageContent = start < filtered.size() ? filtered.subList(start, end) : java.util.Collections.emptyList();
    
    return new org.springframework.data.domain.PageImpl<>(
        pageContent, pageable, filtered.size());
  }

  public Page<Message> getAllMessages(Pageable pageable) {
    return messageRepository.findAll(pageable);
  }

  @Transactional
  public void deleteMessage(UUID messageId, String reason) {
    Message message =
        messageRepository
            .findById(messageId)
            .orElseThrow(() -> new EntityNotFoundException("Message not found"));

    // Log deletion reason before deleting
    log.info("Deleting message {} - Reason: {}", messageId, reason);

    messageRepository.delete(message);
  }

  @Transactional
  public void warnUser(UUID userId, UserWarnRequestDTO request) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

    // Verify user exists (already done above)
    // TODO: Send warning notification/email to user
    // TODO: Log warning in user activity
    log.info("Warning user {} ({}) - Message: {}, Reason: {}", 
        userId, user.getEmail(), request.message(), request.reason());
  }

  public String exportMessages(ExportRequestDTO request) {
    // TODO: Implement export via ExportService
    return "Export functionality will be implemented with ExportService";
  }
}
