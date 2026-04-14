package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class Main {

    private static final String DECRYPTION = "dec";
    private static final String ENCRYPTION = "enc";
    private static final String DEFAULT_MODE = ENCRYPTION;
    private static final String DEFAULT_ALGORITHM = "shift";
    private static final int DEFAULT_STEP = 0;

    public static void main(String[] args) {
        try {
            Map<String, String> arguments = parseArgs(args);

            String inParam   = arguments.get("-in");
            String outParam  = arguments.get("-out");
            String dataParam = arguments.get("-data");
            String mode      = getOrDefault(arguments, "-mode", DEFAULT_MODE);
            String algorithm = getOrDefault(arguments, "-alg",  DEFAULT_ALGORITHM);
            int key          = Integer.parseInt(getOrDefault(arguments, "-key", String.valueOf(DEFAULT_STEP)));

            String input = resolveInput(dataParam, inParam);

            EncodingDecodingContext context = new EncodingDecodingContext(algorithm, key);

            String result = switch (mode) {
                case ENCRYPTION -> context.encode(input);
                case DECRYPTION -> context.decode(input);
                default -> throw new IllegalArgumentException("Unknown mode: " + mode);
            };

            writeOutput(result, outParam);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }


    private static String getOrDefault(Map<String, String> map, String key, String fallback) {
        String value = map.get(key);
        return (value != null) ? value : fallback;
    }

    private static String resolveInput(String data, String filePath) {
        if (data != null) return data;
        if (filePath != null) {
            try {
                return Files.readString(Paths.get(filePath));
            } catch (IOException e) {
                throw new RuntimeException("Error: cannot read from " + filePath, e);
            }
        }
        return "";
    }

    private static void writeOutput(String result, String filePath) {
        if (filePath != null) {
            try {
                Files.writeString(Paths.get(filePath), result);
            } catch (IOException e) {
                throw new RuntimeException("Error: cannot write to " + filePath, e);
            }
        } else {
            System.out.println(result);
        }
    }

    private static Map<String, String> parseArgs(String[] args) {
        if (args.length % 2 != 0) {
            throw new IllegalArgumentException("Error: wrong count of input parameters");
        }

        Map<String, String> map = new java.util.HashMap<>();
        for (int i = 0; i < args.length; i += 2) {
            map.put(args[i], args[i + 1]);
        }
        return map;
    }
}