package com.example.toyneworkproject.domain;

import java.util.Objects;
import java.util.UUID;

public class User extends Entity<UUID> {

    private String firstName;
    private String lastName;
    private String email;
    private long nanoSecondsOnline;

    public long getNanoSecondsOnline() {
        return nanoSecondsOnline;
    }

    public void setNanoSecondsOnline(long nanoSecondsOnline) {
        this.nanoSecondsOnline = nanoSecondsOnline;
    }

    private UUID userID;

    @Override
    public String toString() {
        return firstName + " " + lastName + " " + email;
    }



    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        nanoSecondsOnline = 0;
        userID = UUID.randomUUID();
        super.setId(userID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userID, user.userID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userID);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
        super.setId(userID);
    }
}
