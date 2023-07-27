package com.example.demo.costomExceptions;

public class UserNotFoundException extends Exception{
    public UserNotFoundException(String massage){
        super(massage);
    }
}
