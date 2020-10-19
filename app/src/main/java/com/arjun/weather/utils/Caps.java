package com.arjun.weather.utils;

public  class Caps {

    // Method to convert the string
    public String capitalizeWord(String str) {
        StringBuilder s = new StringBuilder();

        /*Declare a character of space
         To identify that the next character is the starting
         of a new word
        */

        char ch = ' ';
        for (int i = 0; i < str.length(); i++) {

            if (ch == ' ' && str.charAt(i) != ' ')
                s.append(Character.toUpperCase(str.charAt(i)));
            else
                s.append(str.charAt(i));
            ch = str.charAt(i);
        }

        // Return the string with trimming
        return s.toString().trim();
    }

}
