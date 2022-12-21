package com.example.toyneworkproject.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Message extends Entity<UUID> {

    private UUID fromUserUUID;

    private UUID toUserUUID;

    private String messageContent;

    private LocalDateTime sentTime;

    public Message(UUID fromUserUUID, UUID toUserUUID, String messageContent, LocalDateTime sentTime) {
        this.fromUserUUID = fromUserUUID;
        this.toUserUUID = toUserUUID;
        this.messageContent = messageContent;
        this.sentTime = sentTime;
        super.setId(UUID.randomUUID());
    }

    public UUID getFromUserUUID() {
        return fromUserUUID;
    }

    public UUID getToUserUUID() {
        return toUserUUID;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public LocalDateTime getSentTime() {
        return sentTime;
    }
}
