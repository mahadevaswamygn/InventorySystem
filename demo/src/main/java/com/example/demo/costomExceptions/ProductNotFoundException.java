package com.example.demo.costomExceptions;

public class ProductNotFoundException extends Exception{
    public ProductNotFoundException(String massage){
        super(massage);
    }

}
