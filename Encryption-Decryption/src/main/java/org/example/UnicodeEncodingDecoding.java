package org.example;

public class UnicodeEncodingDecoding implements EncodingDecodingStrategy {

    private int step = 0;


    UnicodeEncodingDecoding(int step){
        this.step = step;
    }

    @Override
    public String encode(String inputValue) {
        char[] chars = inputValue.toCharArray();
        for(int i =0; i<inputValue.length(); i++){
            chars[i] = (char) (chars[i] + step);
        }
        return new String(chars);
    }

    @Override
    public String decode(String inputValue) {
        char[] chars = inputValue.toCharArray();
        for(int i =0; i<inputValue.length(); i++){
            chars[i] = (char) (chars[i] - step);
        }
        return new String(chars);
    }
}
