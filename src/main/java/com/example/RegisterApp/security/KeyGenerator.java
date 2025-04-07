package com.example.RegisterApp.security;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;

public class KeyGenerator {
    public static void main(String[] args) {
        String key = Keys.secretKeyFor(SignatureAlgorithm.HS256).toString();
        System.out.println(key);
    }
}