package com.example.kubico.infrastructure.mapper;

import com.example.kubico.domain.model.Conversation;
import com.example.kubico.domain.model.Message;
import com.example.kubico.domain.model.User;
import com.example.kubico.infrastructure.dto.MessageCreateDTO;
import com.example.kubico.infrastructure.dto.MessageResponseDTO;

public class MessageMapper {

  private MessageMapper() {}

  public static MessageResponseDTO toResponse(Message message) {
    return new MessageResponseDTO(
        message.getId(),
        message.getConversation().getId(),
        message.getSender(),
        message.getContent(),
        message.getIsRead(),
        message.getCreatedAt());
  }

  public static Message toEntity(MessageCreateDTO dto, Conversation conversation, User sender) {
    Message message = new Message();
    message.setConversation(conversation);
    message.setSender(sender);
    message.setContent(dto.content());
    message.setIsRead(false);
    return message;
  }
}

