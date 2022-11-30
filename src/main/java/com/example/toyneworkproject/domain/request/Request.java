package com.example.toyneworkproject.domain.request;

import com.example.toyneworkproject.domain.Entity;
import com.example.toyneworkproject.utils.pairDataStructure.OrderPair;
import com.example.toyneworkproject.utils.pairDataStructure.Pair;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Request extends Entity<OrderPair<UUID,UUID>> {
    UUID sentFromUserUUID;
    UUID sentToUserUUID;
    LocalDateTime sentTime;
    RequestStatus requestStatus;

    public Request(UUID sentFromUserUUID, UUID sentToUserUUID) {
        super();
        this.sentFromUserUUID = sentFromUserUUID;
        this.sentToUserUUID = sentToUserUUID;

        super.setId(new OrderPair<>(sentFromUserUUID,sentToUserUUID));


        requestStatus = RequestStatus.REQUEST_SENT;

        sentTime = LocalDateTime.now();
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
}
