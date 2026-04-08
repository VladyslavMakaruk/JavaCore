package org.example.util.inputValidation;

import org.example.util.Args;

import java.util.regex.Matcher;

public class ArgsValidator {
    public static String invalidInput = "provided request data are not valid";
    public static boolean validateArgs(Args argument){
        if (argument.getCommandType() == null || argument.getCommandType().isEmpty()){
            return false;
        }
        for (Command command : Command.values()) {
            Matcher matcher = command.getPattern().matcher(argument.getCommandType().trim());
            if (matcher.matches()){ // matches() -> find()
                switch (command) {
                    case GET, DELETE -> {
                        return isPersist(argument.getCommandKey());
                    }
                    case SET -> {
                        return isPersist(argument.getCommandKey()) && isPersist(argument.getCommandMessage());
                    }
                    case EXIT -> {
                        return true;
                    }
                };
            }
        }
        return false;
    }
    private static boolean isPersist(String inputValue){
        return inputValue != null && !inputValue.isEmpty();
    }
}




