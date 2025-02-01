package com.savitech.fintab.util;

import org.springframework.stereotype.Component;

import java.util.Random;


@Component
public class RandomStringGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private static final String CHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static final String CODE = "0123456789";

    public String generateKey(int length){

        return getString(length, CHARACTERS);
    }

    public String generateReference(int length){

        return getString(length, CHAR);
    }

    public String generateCode(int length){
        return getString(length, CODE);
    }

    private String getString(int length, String FORMAT) {
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(FORMAT.length());
            char randomChar = FORMAT.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }
}
