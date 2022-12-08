package com.example.toyneworkproject.domain;

import java.time.LocalDateTime;
import java.util.UUID;

/*
For table view purposes
 */
public class UserFriendshipWrapper {







    private User user;
    private Friendship friendship;
    public UserFriendshipWrapper(User u, Friendship f){
        user = u;
        friendship = f;

        firstName = u.getFirstName();
        lastName =  u.getLastName();
        email = u.getEmail();
        startedDate = f.getStartedFrom();


    }


    private String firstName;
    private String lastName;

    private String email;

    public String getEmail() {
        return email;
    }

    private LocalDateTime startedDate;

    public User getUser() {
        return user;
    }

    public Friendship getFriendship() {
        return friendship;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDateTime getStartedDate() {
        return startedDate;
    }
}
