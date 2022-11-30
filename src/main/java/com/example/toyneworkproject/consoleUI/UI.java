package com.example.toyneworkproject.consoleUI;

import com.example.toyneworkproject.domain.Friendship;
import com.example.toyneworkproject.domain.User;
import com.example.toyneworkproject.service.Service;

import java.lang.reflect.Method;
import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

public class UI {
    private Service srv;

    public UI(Service srv) {
        this.srv = srv;
    }

    private final String[] methods={"exitApp","addUser","addFriendship","deleteUser",
    "deleteFriendship","updateUser","showUsers","showFriendships",
    "numberOfCommunities","mostSociableCommunity"};

    private void showMenu(){
        System.out.println("0.Exit program");
        System.out.println("1.Add user\n");
        System.out.println("2.Add friendship\n");
        System.out.println("3.Delete user\n");
        System.out.println("4.Delete friendship\n");
        System.out.println("5.Update user\n");
        System.out.println("6.Show users\n");
        System.out.println("7.Show friendships\n");
        System.out.println("8.Number of communities\n");
        System.out.println("9.Most sociable community\n");
    }
    private void showUsers(){
        for(User u : srv.getUsers()){
            System.out.println(u.toString());
        }
    }

    private void showFriendships(){
        try{
            for(Friendship f : srv.getFriendships()){
                System.out.println(srv.findOne(f.getFirstUserID()).toString() +
                        " este prieten cu " +
                        srv.findOne(f.getSecondUserID()));
            }
        }catch(Exception Ex){
            System.out.println("There was an error when trying to find an user");
        }
    }
    private int inputOption() {
        try {
            Scanner sc = new Scanner(System.in);
            String option = sc.nextLine();
            int intOption = Integer.parseInt(option);

            if(intOption<0 || intOption>9){
                throw new IllegalArgumentException("Invalid Option");
            }
            return intOption;

        } catch (Exception Ex) {
            System.out.println("Invalid option!");
            return -1;
        }
    }
    private void runInputOption(int option){
        try{
            Method method = this.getClass().getDeclaredMethod(methods[option]);
            method.setAccessible(true);
            method.invoke(this);
        }catch(Exception Ex){
            Ex.printStackTrace();
        }
    }

    private void updateUser(){
        try{
            Scanner sc = new Scanner(System.in);
            System.out.println("Introduce the email for the user you want to edit");
            String email = sc.nextLine();
            System.out.println("Introduce the new firstname");
            String newFirstName = sc.nextLine();
            System.out.println("Introduce the new lastname");
            String newLasttName = sc.nextLine();
            System.out.println("Introduce the new email");
            String newEmail = sc.nextLine();

            srv.updateUser(email,newFirstName,newLasttName,newEmail);
        }catch(Exception Ex){
            System.out.println(Ex.getMessage());
        }
    }
    private void addUser(){
        try{

            Scanner sc = new Scanner(System.in);
            System.out.println("Give firstName ");
            String firstName = sc.nextLine();
            System.out.println("Give lastName ");
            String lastName = sc.nextLine();
            System.out.println("Give email ");
            String email = sc.nextLine();

            srv.addUser(firstName,lastName,email);
        }catch(Exception Ex){
            System.out.println(Ex.getMessage()+"\n");
        }
    }
    private int multipleMatchesOption(String stringToMatch) throws Exception {
        Scanner sc = new Scanner(System.in);
        ArrayList<User> userThatMatch = srv.getUsersThatMatch(stringToMatch);
        System.out.println("Multiple users have matched your search. Please give an option\n");
        int index = 0;
        for(User u : userThatMatch){
            System.out.println(index +". " + u.toString());
        }
        int option = Integer.parseInt(sc.nextLine());
        if(option > userThatMatch.size() || option<1)
            throw new Exception("Invalid option");

        return option;
    }
    private UUID findMatchingUser(String stringToMatch) throws Exception {
        ArrayList<User> userThatMatch = srv.getUsersThatMatch(stringToMatch);
        if (userThatMatch.size() == 0) {
            throw new Exception("No user match the given string");
        } else if (userThatMatch.size() == 1) {
            return userThatMatch.get(0).getUserID();
        } else {
            return userThatMatch.get(multipleMatchesOption(stringToMatch)).getUserID();
        }
    }

    private void deleteFriendship(){
        try{
            Scanner sc = new Scanner(System.in);

            System.out.println("Give first user string");
            String stringFirstUser = sc.nextLine();
            UUID firstUser = findMatchingUser(stringFirstUser);

            System.out.println("Give second user string");
            String stringSecondUser = sc.nextLine();
            UUID secondUser = findMatchingUser(stringSecondUser);

            srv.deleteFriendship(firstUser,secondUser);
        }catch(Exception Ex){
            System.out.println(Ex.getMessage());
        }
    }
    private void addFriendship(){
        try{
            Scanner sc = new Scanner(System.in);

            System.out.println("Give first user string");
            String firstUserString = sc.nextLine();
            UUID firstUserUUID = findMatchingUser(firstUserString);

            System.out.println("Give second user string");
            String secondUserString = sc.nextLine();
            UUID secondUserFriendship = findMatchingUser(secondUserString);
            srv.addFriendship(firstUserUUID,secondUserFriendship);

        }catch(Exception Ex){
            System.out.println(Ex.getMessage());
        }
    }
    private void deleteUser(){
        try{
            System.out.println("Give a string");
            Scanner sc = new Scanner(System.in);
            srv.deleteUserByID(findMatchingUser(sc.nextLine()));
        }catch(Exception Ex){
            System.out.println(Ex.getMessage());
        }
    }
    private void exitApp(){
        System.exit(0);
    }
    public void runApp(){
        while(true){
            showMenu();
            runInputOption(inputOption());
        }
    }
}
