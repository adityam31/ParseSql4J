package org.parsesql4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Line {
    private static final Pattern NAME_PATTERN = Pattern.compile("-+ name\\s*:\\s*([^-]+)\\s*-+");
    private static final Pattern COMMENT_PATTERN = Pattern.compile("^-+.*$");
    private static final Pattern INLINE_COMMENT_PATTERN = Pattern.compile("--.*$");

    private String value;
    private LineType type;

    private Line(String value, LineType type) {
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public LineType getType() {
        return type;
    }

    public static Line getInstance(final String line) {
        String trimVal = line.trim();

        // return blank line, ignore it
        if(trimVal.isBlank()) {
            return new Line("", LineType.BLANK);
        }

        // If name tag found, return name of the query
        Matcher nameMatcher = NAME_PATTERN.matcher(trimVal);
        if(nameMatcher.matches()) {
            return new Line(nameMatcher.group(1).trim(), LineType.NAME_TAG);
        }

        // If comment, ignore it
        Matcher commentMatcher = COMMENT_PATTERN.matcher(trimVal);
        if(commentMatcher.matches()) {
            return new Line("", LineType.COMMENT);
        }

        // return query with inline comments removed
        Matcher inlineCommentMatcher = INLINE_COMMENT_PATTERN.matcher(trimVal);
        return new Line(inlineCommentMatcher.replaceAll(""), LineType.QUERY);
    }

    enum LineType {
        QUERY, NAME_TAG, COMMENT, BLANK;
    }
}
