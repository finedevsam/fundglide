package com.savitech.fintab.util;

import org.springframework.stereotype.Component;

@Component
public class AmountToWords {
    private static final String[] UNITS = {
            "", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine",
            "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen",
            "seventeen", "eighteen", "nineteen"
    };

    private static final String[] TENS = {
            "", "", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"
    };

    private static final String[] THOUSANDS = {"", "thousand", "million", "billion", "trillion"};

    public String convertToWords(double amount) {
        if (amount == 0) {
            return "zero naira";
        }

        long amountInCents = Math.round(amount * 100);
        int naira = (int) (amountInCents / 100);
        int kobo = (int) (amountInCents % 100);

        String amountInWords = "";

        if (naira > 0) {
            amountInWords = convert(naira) + " naira";
        }

        if (kobo > 0) {
            amountInWords += " and " + convert(kobo) + " kobo " + (kobo == 1 ? "" : "s");
        }

        return amountInWords.trim();
    }

    private static String convert(int number) {
        if (number < 20) {
            return UNITS[number];
        }

        if (number < 100) {
            return TENS[number / 10] + " " + UNITS[number % 10];
        }

        StringBuilder words = new StringBuilder();

        for (int i = 0; number > 0; i++) {
            if (number % 1000 != 0) {
                words.insert(0, convert(number % 1000) + " " + THOUSANDS[i] + " ");
            }
            number /= 1000;
        }

        return words.toString();
    }
}

