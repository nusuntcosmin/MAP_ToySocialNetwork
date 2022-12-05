package com.example.toyneworkproject.domain;

import com.example.toyneworkproject.domain.request.Request;

import java.time.LocalDateTime;

/**
 * For tableview purposes
 */
public class UserRequestWrapper {

    public String userName;
    public String userEmail;
    public String requestSentTime;

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getRequestSentTime() {
        return requestSentTime;
    }

    public UserRequestWrapper(User user, Request request){
        userName = user.getFirstName() + " " + user.getLastName();
        userEmail = user.getEmail();
        requestSentTime = request.getSentTime().toString();
    }
}
