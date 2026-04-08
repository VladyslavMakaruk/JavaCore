package org.example.util.inputValidation;

import java.util.regex.Pattern;

public enum Command {
    GET("get"),
    SET("set"),
    DELETE("delete"),
    EXIT("exit");


    private final Pattern pattern;

    Command(String regex) {
        this.pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    }

    public Pattern getPattern() { return pattern; }

    @Override
    public String toString(){
        return this.pattern.toString();
    }
}