package com.example.demo.costomExceptions;

public class ProductQuantityNotExistException extends Exception{
    public ProductQuantityNotExistException(String massage){
        super(massage);
    }
}
