package com.example.frotamotors.infrastructure.web;

import com.example.frotamotors.domain.model.Conversation;
import com.example.frotamotors.domain.model.Message;
import com.example.frotamotors.domain.service.MessageService;
import com.example.frotamotors.infrastructure.dto.ConversationResponseDTO;
import com.example.frotamotors.infrastructure.dto.ExportRequestDTO;
import com.example.frotamotors.infrastructure.dto.MessageCreateDTO;
import com.example.frotamotors.infrastructure.dto.MessageResponseDTO;
import com.example.frotamotors.infrastructure.dto.PageResponseDTO;
import com.example.frotamotors.infrastructure.dto.UserBlockRequestDTO;
import com.example.frotamotors.infrastructure.mapper.MessageMapper;
import com.example.frotamotors.infrastructure.util.SecurityUtils;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/v1/messages")
@RequiredArgsConstructor
@Slf4j
public class MessageController {

  @Autowired private MessageService messageService;

  @PostMapping("/conversation")
  public ResponseEntity<ConversationResponseDTO> getOrCreateConversation(
      @RequestParam UUID user1Id, @RequestParam UUID user2Id) {
    Conversation conversation = messageService.getOrCreateConversation(user1Id, user2Id);
    ConversationResponseDTO response =
        new ConversationResponseDTO(
            conversation.getId(),
            conversation.getUser1(),
            conversation.getUser2(),
            conversation.getLastMessageAt(),
            conversation.getCreatedAt());
    return ResponseEntity.ok(response);
  }

  @GetMapping("/conversation/{conversationId}")
  public ResponseEntity<PageResponseDTO<MessageResponseDTO>> getMessages(
      @PathVariable UUID conversationId,
      @PageableDefault(
              size = 50,
              sort = "createdAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {
    Page<Message> page = messageService.getMessagesByConversation(conversationId, pageable);

    List<MessageResponseDTO> content =
        page.getContent().stream().map(MessageMapper::toResponse).collect(Collectors.toList());

    PageResponseDTO<MessageResponseDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/user/{userId}/conversations")
  public ResponseEntity<PageResponseDTO<ConversationResponseDTO>> getConversations(
      @PathVariable UUID userId,
      @PageableDefault(
              size = 20,
              sort = "lastMessageAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {
    Page<Conversation> page = messageService.getConversationsByUser(userId, pageable);

    List<ConversationResponseDTO> content =
        page.getContent().stream()
            .map(
                c ->
                    new ConversationResponseDTO(
                        c.getId(),
                        c.getUser1(),
                        c.getUser2(),
                        c.getLastMessageAt(),
                        c.getCreatedAt()))
            .collect(Collectors.toList());

    PageResponseDTO<ConversationResponseDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/conversation/{conversationId}/unread-count")
  public ResponseEntity<Long> getUnreadCount(
      @PathVariable UUID conversationId, @RequestParam UUID userId) {
    Long count = messageService.getUnreadMessageCount(conversationId, userId);
    return ResponseEntity.ok(count);
  }

  @PostMapping
  public ResponseEntity<MessageResponseDTO> sendMessage(@RequestBody MessageCreateDTO dto) {
    Message saved = messageService.sendMessage(dto);
    return ResponseEntity.ok(MessageMapper.toResponse(saved));
  }

  @PostMapping("/conversation/{conversationId}/read")
  public ResponseEntity<Void> markAsRead(
      @PathVariable UUID conversationId, @RequestParam UUID userId) {
    messageService.markMessagesAsRead(conversationId, userId);
    return ResponseEntity.ok().build();
  }

  @GetMapping("{id}")
  public ResponseEntity<MessageResponseDTO> getById(@PathVariable UUID id) {
    Message message = messageService.getById(id);
    return ResponseEntity.ok(MessageMapper.toResponse(message));
  }

  @PostMapping("/block")
  public ResponseEntity<Void> blockUser(
      @RequestParam UUID blockedId,
      @Valid @RequestBody(required = false) UserBlockRequestDTO request) {
    UUID blockerId = SecurityUtils.getCurrentUserId();
    String reason = request != null ? request.reason() : null;
    messageService.blockUser(blockerId, blockedId, reason);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/block")
  public ResponseEntity<Void> unblockUser(@RequestParam UUID blockedId) {
    UUID blockerId = SecurityUtils.getCurrentUserId();
    messageService.unblockUser(blockerId, blockedId);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/admin/conversations")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  public ResponseEntity<PageResponseDTO<ConversationResponseDTO>> getAllConversations(
      @PageableDefault(
              size = 20,
              sort = "lastMessageAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {
    Page<Conversation> page = messageService.getAllConversations(pageable);

    List<ConversationResponseDTO> content =
        page.getContent().stream()
            .map(
                c ->
                    new ConversationResponseDTO(
                        c.getId(),
                        c.getUser1(),
                        c.getUser2(),
                        c.getLastMessageAt(),
                        c.getCreatedAt()))
            .collect(Collectors.toList());

    PageResponseDTO<ConversationResponseDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/admin/messages")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  public ResponseEntity<PageResponseDTO<MessageResponseDTO>> getAllMessages(
      @PageableDefault(
              size = 50,
              sort = "createdAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {
    Page<Message> page = messageService.getAllMessages(pageable);

    List<MessageResponseDTO> content =
        page.getContent().stream().map(MessageMapper::toResponse).collect(Collectors.toList());

    PageResponseDTO<MessageResponseDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @PostMapping("/export")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  public ResponseEntity<java.util.Map<String, String>> exportMessages(
      @Valid @RequestBody ExportRequestDTO request) {
    String result = messageService.exportMessages(request);
    return ResponseEntity.ok(java.util.Map.of("message", result));
  }
}
