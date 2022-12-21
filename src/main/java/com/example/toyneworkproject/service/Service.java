package com.example.toyneworkproject.service;

import com.example.toyneworkproject.domain.*;
import com.example.toyneworkproject.exceptions.RepositoryException;
import com.example.toyneworkproject.exceptions.ServiceException;
import com.example.toyneworkproject.exceptions.ValidationException;
import com.example.toyneworkproject.repository.Repository;
import com.example.toyneworkproject.repository.database.RepositoryDatabaseMessage;
import com.example.toyneworkproject.repository.database.RepositoryDatabaseRequest;
import com.example.toyneworkproject.utils.pairDataStructure.OrderPair;
import com.example.toyneworkproject.utils.pairDataStructure.Pair;
import com.example.toyneworkproject.validators.UserValidator;
import com.example.toyneworkproject.validators.Validator;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Service {
    Repository<UUID, User> repoUser;
    Repository<Pair<UUID,UUID>, Friendship> repoFriendship;

    RepositoryDatabaseRequest repoRequest;

    Validator<User> userValidator;

    RepositoryDatabaseMessage repoMessages;

    public void addMessage(UUID fromUserUUID, UUID toUserUUID, String messageContent) throws RepositoryException {
        repoMessages.save(new Message(fromUserUUID,toUserUUID,messageContent,LocalDateTime.now()));
    }

    private Iterable<Message> getMessagesBetweenUsers(UUID firstUserUUID, UUID secondUserUUID){
        return repoMessages.getMessagesBetweenUsers(firstUserUUID,secondUserUUID);
    }

    public ArrayList<Message> getOrderedByTimeMessagesBetweenUsers(UUID firstUserUUID, UUID secondUserUUID){

        ArrayList<Message> messagesBetweenUsers = (ArrayList<Message>) getMessagesBetweenUsers(firstUserUUID,secondUserUUID);

        messagesBetweenUsers.sort(Comparator.comparing(Message::getSentTime));

        return messagesBetweenUsers;

    }

    public void updateRequestStatus(OrderPair<UUID,UUID>requestID, String newRequestStatus) throws RepositoryException {
        repoRequest.updateRequestStatus(requestID,newRequestStatus);
    }
    public void saveRequest(UUID firstUserUUID, UUID secondUserUUID, String requestStatus, LocalDateTime sentTime) throws RepositoryException, ServiceException {
        if(firstUserUUID.equals(secondUserUUID))
            throw new ServiceException("You cannot sent a request to yourself");

        if(friendShipExist(new Pair<>(firstUserUUID,secondUserUUID)))
            throw new ServiceException("You are already friends with this user");
        Request requestToAdd = new Request(firstUserUUID,secondUserUUID,sentTime,requestStatus);


        for(Request request : getRequests()){
            if(request.equals(requestToAdd))
                throw new ServiceException("Already sent an request to this user");

            if(request.getId().getFirstElement().equals(secondUserUUID) &&
                    request.getId().getSecondElement().equals(firstUserUUID))
                throw new ServiceException("This user sent you a friendship request");
        }
        repoRequest.save(new Request(firstUserUUID,secondUserUUID,sentTime,requestStatus));

    }

    public List<User> getUsersWithout(UUID userUUID){
        ArrayList<User> users = new ArrayList<>((Collection<User>)getUsers());

        users.removeIf(user->
            user.getUserID().equals(userUUID)
        );

        return users;
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

    public List<User> getUserFriendsForUser(UUID userUUID) throws RepositoryException {
        ArrayList<Friendship> friendships = new ArrayList<>((Collection<Friendship>) getFriendships());

        friendships = (ArrayList<Friendship>) friendships
                .stream()
                .filter(f->f.getFirstUserID().equals(userUUID) || f.getSecondUserID().equals(userUUID))
                .collect(Collectors.toList());

        ArrayList<User> userFriends = new ArrayList<>();

        for(Friendship f : friendships){
            UUID friendUUID;
            if(f.getFirstUserID().equals(userUUID))
                friendUUID = f.getSecondUserID();
            else
                friendUUID = f.getFirstUserID();

            userFriends.add(findOne(friendUUID));
        }

        return userFriends;

    }
    public List<UserFriendshipWrapper> getFriendsForUser(UUID userUUID) throws RepositoryException {
        ArrayList<Friendship> friendships = new ArrayList<>((Collection<Friendship>) getFriendships());

        friendships = (ArrayList<Friendship>) friendships
                .stream()
                .filter(f->f.getFirstUserID().equals(userUUID) || f.getSecondUserID().equals(userUUID))
                .collect(Collectors.toList());

        ArrayList<UserFriendshipWrapper> userFriendshipWrappers = new ArrayList<>();
        for(Friendship f : friendships){
            UUID friendUUID;
            if(f.getFirstUserID().equals(userUUID))
                friendUUID = f.getSecondUserID();
            else
                friendUUID = f.getFirstUserID();
            userFriendshipWrappers.add(new UserFriendshipWrapper(findOne(friendUUID),f));
        }

        return userFriendshipWrappers;

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
    public void updateOnlineTime(User loggedUser,long start,long finish) throws RepositoryException {
            loggedUser.setNanoSecondsOnline(loggedUser.getNanoSecondsOnline() + finish - start);
            repoUser.update(loggedUser);
    }
    public Service(Repository<UUID, User> repoUser, Repository<Pair<UUID, UUID>, Friendship> repoFriendship,RepositoryDatabaseRequest repoRequest,RepositoryDatabaseMessage repoMessages) {
        this.repoUser = repoUser;
        this.repoMessages = repoMessages;
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
    public void updateUser(String email,String newFirstName, String newLastName, String newEmail) throws Exception {

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
        return repoRequest.findAll();
    }


}
