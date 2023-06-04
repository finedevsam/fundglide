package com.savitech.fintab.util;

import org.springframework.stereotype.Component;

import java.util.Random;


@Component
public class RandomStringGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public String generateKey(int length){

        return getString(length);
    }


    private static final String CHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public String generateReference(int length){

        return getString(length);
    }

    private String getString(int length) {
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }
}
