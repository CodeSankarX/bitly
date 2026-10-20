package com.project.bitly.links;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.UUID;

public class ShortCode {


    private static final String ALPHABET =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generate() {
        StringBuilder result = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            result.append(
                    ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length()))
            );
        }

        return result.toString();
    }
}