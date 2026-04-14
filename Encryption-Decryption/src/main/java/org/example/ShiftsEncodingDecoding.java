package org.example;

public class ShiftsEncodingDecoding implements EncodingDecodingStrategy {
    /*
    Remember that in case of shift, encode only English letters — from "a" to "z" and from "A" to "Z".
    In other words, after "z" comes "a", after "Z" comes "A".
    **/
    private final char lowerCaseLB = 'a';
    private final char lowerCaseUB = 'z';
    private final char lowerCaseDiff = lowerCaseUB - lowerCaseLB;
    private final char upperCaseLB = 'A';
    private final char upperCaseUB = 'Z';
    private final char upperCaseDiff = upperCaseUB - upperCaseLB;
    private int step = 0;


    ShiftsEncodingDecoding(int step){
        this.step = step;
    }
    
    @Override
    public String encode(String inputValue) {
        char[] chars = inputValue.toCharArray();
        for(int i =0; i<inputValue.length(); i++){
            chars[i] = shift(chars[i]);
        }
        return new String(chars);
    }

    @Override
    public String decode(String inputValue) {
        char[] chars = inputValue.toCharArray();
        for(int i =0; i<inputValue.length(); i++){
            chars[i] = deShift(chars[i]);
        }
        return new String(chars);
    }

    private char deShift(char valueToShift) {
        if (String.valueOf(valueToShift).matches("[a-z]")) {
            int range = lowerCaseDiff + 1;
            valueToShift = (char) (lowerCaseLB + ((valueToShift - lowerCaseLB - step) % range + range) % range);
        } else if (String.valueOf(valueToShift).matches("[A-Z]")) {
            int range = upperCaseDiff + 1;
            valueToShift = (char) (upperCaseLB + ((valueToShift - upperCaseLB - step) % range + range) % range);
        }
        return valueToShift;
    }

    private char shift(char valueToShift){
        if (String.valueOf(valueToShift).matches("[a-z]")) {
            valueToShift = (char) ( lowerCaseLB+ ((valueToShift -  lowerCaseLB + step)%(lowerCaseDiff+1)));
        } else if (String.valueOf(valueToShift).matches("[A-Z]")){
            valueToShift = (char) ( upperCaseLB+ ((valueToShift -  upperCaseLB + step)%(upperCaseDiff+1)));
        }
        return valueToShift;
    }
}
