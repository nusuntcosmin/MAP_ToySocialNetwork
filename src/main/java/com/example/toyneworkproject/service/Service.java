package com.example.toyneworkproject.service;

import com.example.toyneworkproject.domain.Friendship;
import com.example.toyneworkproject.domain.User;
import com.example.toyneworkproject.domain.UserRequestWrapper;
import com.example.toyneworkproject.domain.request.Request;
import com.example.toyneworkproject.exceptions.RepositoryException;
import com.example.toyneworkproject.exceptions.ServiceException;
import com.example.toyneworkproject.exceptions.ValidationException;
import com.example.toyneworkproject.repository.Repository;
import com.example.toyneworkproject.utils.pairDataStructure.OrderPair;
import com.example.toyneworkproject.utils.pairDataStructure.Pair;
import com.example.toyneworkproject.validators.UserValidator;
import com.example.toyneworkproject.validators.Validator;

import java.time.LocalDateTime;
import java.util.*;

public class Service {
    Repository<UUID, User> repoUser;
    Repository<Pair<UUID,UUID>, Friendship> repoFriendship;

    Repository<OrderPair<UUID,UUID>, Request> repoRequest;

    Validator<User> userValidator;

    public void saveRequest(UUID firstUserUUID, UUID secondUserUUID, String requestStatus, LocalDateTime sentTime) throws RepositoryException {
        repoRequest.save(new Request(firstUserUUID,secondUserUUID,sentTime,requestStatus));

    }

    public List<Request> getReceivedRequestForUser(UUID userUUID){
        ArrayList<Request> receivedRequests = new ArrayList<>();

        findAllRequest().forEach(
                (Request request) ->{
                    if(request.getId().getSecondElement().equals(userUUID)){
                        receivedRequests.add(request);
                    }
                }
        );
        return receivedRequests;
    }

    public List<UserRequestWrapper> usersThatSentRequestTo(UUID userUUID){
        ArrayList<UserRequestWrapper> users = new ArrayList<>();

        getReceivedRequestForUser(userUUID).forEach(
                (Request request) ->{
                        try {
                            users.add(new UserRequestWrapper(findOne(request.getId().getFirstElement()),request));
                        } catch (RepositoryException e) {
                            throw new RuntimeException(e);
                        }

                }
        );
        return users;
    }

    public List<Request> getSentRequestForUser(UUID userUUID){
        ArrayList<Request> sentRequests = new ArrayList<>();
        findAllRequest().forEach(
                (Request request) ->{
                    if(request.getId().getFirstElement().equals(userUUID)){
                        sentRequests.add(request);
                    }
                }
        );
        return sentRequests;
    }

    public List<UserRequestWrapper> usersThatReceiveRequestsFrom(UUID userUUID){
        ArrayList<UserRequestWrapper> users = new ArrayList<>();

        getSentRequestForUser(userUUID).forEach(
                (Request request) ->{
                        try {
                            users.add(new UserRequestWrapper(findOne(request.getId().getSecondElement()),request));
                        } catch (RepositoryException e) {
                            throw new RuntimeException(e);
                        }
                }
        );
        return users;
    }

    public void deleteRequest(OrderPair<UUID,UUID> requestID) throws RepositoryException {
        repoRequest.delete(requestID);
    }

    public Request findOneRequest(OrderPair<UUID,UUID> requestID) throws RepositoryException {
        return repoRequest.findOne(requestID);
    }

    public Iterable<Request> findAllRequest(){
        return repoRequest.findAll();
    }
    public void updateOnlineTime(User loggedUser,long nanoSecondsTime) throws RepositoryException {
            loggedUser.setNanoSecondsOnline(nanoSecondsTime);
            repoUser.update(loggedUser);
    }
    public Service(Repository<UUID, User> repoUser, Repository<Pair<UUID, UUID>, Friendship> repoFriendship,Repository<OrderPair<UUID,UUID>,Request> repoRequest) {
        this.repoUser = repoUser;
        this.repoFriendship = repoFriendship;
        this.repoRequest = repoRequest;
        userValidator = new UserValidator();
    }
    public boolean emailExists(String email){
        for (User user : repoUser.findAll()) {
            if (user.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }
    public User addUser(String firstName, String lastName, String email) throws ServiceException, ValidationException, RepositoryException {
        if(emailExists(email))
            throw new ServiceException("This email already exists !");

        User user = new User(firstName,lastName,email);
        userValidator.validate(user);
        return repoUser.save(user);
    }
    public User findOne(UUID userID) throws RepositoryException {
        return repoUser.findOne(userID);
    }

    private void deleteUsersFriendships(UUID userUUID) throws RepositoryException {
        for(Friendship f : getFriendships()){
            if(f.isPartOfFriendship(userUUID))
                deleteFriendship(f.getFirstUserID(),f.getSecondUserID());
        }

    }
    public void deleteFriendship(UUID u1,UUID u2) throws RepositoryException {
        if(!friendShipExist(new Pair<>(u1,u2)))
            throw new RuntimeException("Friendship between these users does not exist ");

        repoFriendship.delete(new Pair<>(u1,u2));
    }
    private boolean friendShipExist(Pair<UUID,UUID> friendshipID){
        for (Friendship f : getFriendships()) {
            if (f.getId().equals(friendshipID)) {
                return true;
            }
        }
        return false;
    }
    public void addFriendship(UUID u1, UUID u2) throws ServiceException, RepositoryException {
        if(u1.equals(u2))
            throw new ServiceException("Users cannot be friends with themselves");

        if(friendShipExist(new Pair<>(u1,u2)))
            throw new ServiceException("These users are already friends");

        repoFriendship.save(new Friendship(u1,u2));
    }
    public void deleteUserByID(UUID userIDToDelete) throws RepositoryException {
        repoUser.delete(userIDToDelete);
        deleteUsersFriendships(userIDToDelete);

    }
    public User findByEmail(String email) throws ServiceException {
        for(User u: getUsers()){
            if(u.getEmail().equals(email))
                return u;
        }
        throw new ServiceException("An user with this email was not registered");
    }
    public void updateUser(String email,String newFirstName, String newLastName, String newEmail) throws ServiceException, RepositoryException, ValidationException {

        if(!emailExists(email))
            throw new ServiceException("This email does not exist");
        if(emailExists(newEmail) && !email.equals(newEmail)){
            throw new ServiceException("This email is already taken");
        }
        User u = new User(newFirstName,newLastName,newEmail);
        //userValidator.validate(u);
        u.setUserID(findByEmail(email).getUserID());
        repoUser.update(u);
    }

    public ArrayList<User> getUsersThatMatch(String stringToMatch){
        ArrayList<User> usersThatMatch = new ArrayList<>();
        for(User u : getUsers()){
            if((u.getFirstName() + " " +  u.getLastName()).toLowerCase().contains(stringToMatch.toLowerCase()))
                usersThatMatch.add(u);
        }
        return usersThatMatch;
    }
    public Iterable<User> getUsers(){
        return repoUser.findAll();
    }
    public Iterable<Friendship> getFriendships(){
        return repoFriendship.findAll();
    }

    public Iterable<Request> getRequests() {
        return null;
    }


}
