package org.example;

public class EncodingDecodingContext {
    private final EncodingDecodingStrategy strategy;

    EncodingDecodingContext(String strategyName, int step){
        this.strategy = switch (strategyName){
            case "shift" -> new ShiftsEncodingDecoding(step);
            case "unicode" -> new UnicodeEncodingDecoding(step);
            default -> throw new RuntimeException(String.format("There is no %s enc/dec algorithm", strategyName));
        };
    }

    public String encode(String inputValue){
        return this.strategy.encode(inputValue);
    }
    public String decode(String inputValue){
        return this.strategy.decode(inputValue);
    }
}
