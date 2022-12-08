package com.example.toyneworkproject.domain;

import com.example.toyneworkproject.utils.pairDataStructure.OrderPair;

import java.util.UUID;

/**
 * For tableview purposes
 */
public class UserRequestWrapper {

    public String userName;
    private String userEmail;
    private String requestSentTime;

    private User user;

    public User getUser() {
        return user;
    }

    public Request getRequest() {
        return request;
    }

    private Request request;



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

        this.user = user;
        this.request = request;
        userName = user.getFirstName() + " " + user.getLastName();
        userEmail = user.getEmail();
        requestSentTime = request.getSentTime().toString();

    }
}
