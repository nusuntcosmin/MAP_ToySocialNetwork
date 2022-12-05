package com.example.toyneworkproject.domain.request;

import com.example.toyneworkproject.domain.Entity;
import com.example.toyneworkproject.utils.pairDataStructure.OrderPair;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Request extends Entity<OrderPair<UUID,UUID>> {
    UUID sentFromUserUUID;
    UUID sentToUserUUID;
    LocalDateTime sentTime;
    String requestStatus;

    public Request(UUID sentFromUserUUID, UUID sentToUserUUID) {
        super();
        this.sentFromUserUUID = sentFromUserUUID;
        this.sentToUserUUID = sentToUserUUID;

        super.setId(new OrderPair<>(sentFromUserUUID,sentToUserUUID));
        requestStatus = "Sent";
        sentTime = LocalDateTime.now();
    }

    public Request(UUID sentFromUserUUID, UUID sentToUserUUID, LocalDateTime sentTime,String requestStatus) {
        super();
        this.sentFromUserUUID = sentFromUserUUID;
        this.sentToUserUUID = sentToUserUUID;

        super.setId(new OrderPair<>(sentFromUserUUID,sentToUserUUID));
        this.requestStatus = requestStatus;
        this.sentTime = sentTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request thatRequest = (Request) o;
        return this.getId().equals(thatRequest.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(sentFromUserUUID, sentToUserUUID);
    }

    public LocalDateTime getSentTime() {
        return sentTime;
    }

    public String getRequestStatus() {
        return requestStatus;
    }
}
