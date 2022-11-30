package com.example.toyneworkproject.service;

import com.example.toyneworkproject.domain.Friendship;
import com.example.toyneworkproject.domain.User;
import com.example.toyneworkproject.exceptions.RepositoryException;
import com.example.toyneworkproject.exceptions.ServiceException;
import com.example.toyneworkproject.exceptions.ValidationException;
import com.example.toyneworkproject.repository.Repository;
import com.example.toyneworkproject.utils.pairDataStructure.Pair;
import com.example.toyneworkproject.validators.UserValidator;
import com.example.toyneworkproject.validators.Validator;

import java.util.ArrayList;
import java.util.UUID;

public class Service {
    Repository<UUID, User> repoUser;
    Repository<Pair<UUID,UUID>, Friendship> repoFriendship;

    Validator<User> userValidator;

    public Service(Repository<UUID, User> repoUser, Repository<Pair<UUID, UUID>, Friendship> repoFriendship) {
        this.repoUser = repoUser;
        this.repoFriendship = repoFriendship;
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
    public void addUser(String firstName, String lastName, String email) throws ServiceException, ValidationException, RepositoryException {
        if(emailExists(email))
            throw new ServiceException("This email already exists !");

        User user = new User(firstName,lastName,email);
        userValidator.validate(user);
        repoUser.save(user);
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
        if(!friendShipExist(new Pair(u1,u2)))
            throw new RuntimeException("Friendship between these users does not exist ");

        repoFriendship.delete(new Pair(u1,u2));
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

        if(friendShipExist(new Pair(u1,u2)))
            throw new ServiceException("These users are already friends");

        repoFriendship.save(new Friendship(u1,u2));
    }
    public void deleteUserByID(UUID userIDToDelete) throws RepositoryException {
        repoUser.delete(userIDToDelete);
        deleteUsersFriendships(userIDToDelete);

    }
    public User findByEmail(String email){
        for(User u: getUsers()){
            if(u.getEmail().equals(email))
                return u;
        }
        return null;
    }
    public void updateUser(String email,String newFirstName, String newLastName, String newEmail) throws ServiceException, RepositoryException {

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




}
