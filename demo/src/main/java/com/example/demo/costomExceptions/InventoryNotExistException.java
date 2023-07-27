package com.example.demo.costomExceptions;

public class InventoryNotExistException extends Exception{
    public InventoryNotExistException(String massage){
        super(massage);
    }
}
