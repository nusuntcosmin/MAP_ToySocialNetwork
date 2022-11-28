package com.example.toyneworkproject.domain;

import com.example.toyneworkproject.utils.Pair;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Friendship extends Entity<Pair<UUID,UUID>> {


    private UUID firstUserID;
    private UUID secondUserID;

    private LocalDateTime startedFrom;


    public Friendship(UUID firstUserID, UUID secondUserID) {
        this.firstUserID = firstUserID;
        this.secondUserID = secondUserID;
        this.startedFrom = LocalDateTime.now();
        super.setId(new Pair(firstUserID,secondUserID));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return Objects.equals(firstUserID, that.firstUserID) && Objects.equals(secondUserID, that.secondUserID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstUserID, secondUserID);
    }

    public UUID getFirstUserID() {
        return firstUserID;
    }

    public void setFirstUserID(UUID firstUserID) {
        this.firstUserID = firstUserID;
    }

    public UUID getSecondUserID() {
        return secondUserID;
    }

    public void setSecondUserID(UUID secondUserID) {
        this.secondUserID = secondUserID;
    }


}
