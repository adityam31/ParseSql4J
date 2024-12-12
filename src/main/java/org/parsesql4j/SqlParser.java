package org.parsesql4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class SqlParser {

    private SqlParser() {}

    public static Map<String, String> parse(final String filePath) {
        try (InputStream inputStream = Files.newInputStream(Path.of(filePath))) {
            return parseInputStream(inputStream);
        } catch (Exception ex) {
            throw new RuntimeException("Error while parsing file: " + filePath, ex);
        }
    }

    public static Map<String, String> parse(final InputStream is) {
        try {
            return parseInputStream(is);
        } catch (Exception ex) {
            throw new RuntimeException("Error while processing input stream", ex);
        }
    }

    private static Map<String, String> parseInputStream(final InputStream is) throws Exception {
        Map<String, String> queryMap = new LinkedHashMap<>();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is))) {
            String currentLine;
            String lastTag = "";
            while ((currentLine = bufferedReader.readLine()) != null) {
                Line line = Line.getInstance(currentLine);
                if (line.getType() == Line.LineType.NAME_TAG) {
                    lastTag = line.getValue();
                    queryMap.put(lastTag, "");
                } else if (line.getType() == Line.LineType.QUERY) {
                    String prevVal = queryMap.get(lastTag);
                    if(!prevVal.isBlank()) {
                        queryMap.put(lastTag, queryMap.get(lastTag) + "\n" + line.getValue());
                    } else {
                       queryMap.put(lastTag, line.getValue());
                    }
                }
            }
        }
        return Collections.unmodifiableMap(queryMap);
    }
}
