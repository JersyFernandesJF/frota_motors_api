package com.example.frotamotors.infrastructure.mapper;

import com.example.frotamotors.domain.model.Conversation;
import com.example.frotamotors.domain.model.Message;
import com.example.frotamotors.domain.model.User;
import com.example.frotamotors.infrastructure.dto.MessageCreateDTO;
import com.example.frotamotors.infrastructure.dto.MessageResponseDTO;

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
