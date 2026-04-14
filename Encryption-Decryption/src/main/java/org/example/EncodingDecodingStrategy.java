package org.example;

public interface EncodingDecodingStrategy {
    public abstract String encode(String inputValue);
    public abstract String decode(String inputValue);
}
