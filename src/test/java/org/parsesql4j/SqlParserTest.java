package org.parsesql4j;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SqlParserTest {

    @Test
    void testSqlParser_SingleLine_ShouldReturnMap() {
        // Given
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("sqlparsertest/test01_singleline.sql");
        String GET_ALL_USERS = "SELECT * FROM users;";
        String GET_ALL_USERS_WITH_NAME = "SELECT * FROM users where name = 'Mike';";
        String GET_ALL_USERS_WITH_ID = "SELECT * FROM users where name = 'Mike' and id > 53;";

        // When
        Map<String, String> queryMap = SqlParser.parse(inputStream);

        // Then
        assertNotNull(queryMap);
        assertEquals(3, queryMap.size());
        assertTrue(queryMap.containsKey("GET_ALL_USERS"));
        assertTrue(queryMap.containsKey("GET_ALL_USERS_WITH_NAME"));
        assertTrue(queryMap.containsKey("GET_ALL_USERS_WITH_ID"));
        assertEquals(queryMap.get("GET_ALL_USERS"), GET_ALL_USERS);
        assertEquals(queryMap.get("GET_ALL_USERS_WITH_NAME"), GET_ALL_USERS_WITH_NAME);
        assertEquals(queryMap.get("GET_ALL_USERS_WITH_ID"), GET_ALL_USERS_WITH_ID);
    }

    @Test
    void testSqlParser_Multiline_ShouldReturnMap() {
        // Given
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("sqlparsertest/test02_multiline.sql");

        // When
        Map<String, String> queryMap = SqlParser.parse(inputStream);

        // Then
        assertNotNull(queryMap);
        assertEquals(4, queryMap.size());
        assertTrue(queryMap.containsKey("GET_USERS_WITH_SALARY"));
        assertTrue(queryMap.containsKey("GET_USERS_WITH_JOIN"));
        assertTrue(queryMap.containsKey("EMPLOYEE_QUERY"));
        assertTrue(queryMap.containsKey("COMPLEX_QUERY"));
    }

    @Test
    void testSqlParser_Comments_ShouldReturnMap() {
        // Given
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("sqlparsertest/test03_comments.sql");

        // When
        Map<String, String> queryMap = SqlParser.parse(inputStream);

        // Then
        assertNotNull(queryMap);
        assertEquals(2, queryMap.size());
        assertTrue(queryMap.containsKey("COMPLEX_QUERY_WITH_COMMENTS"));
        assertTrue(queryMap.containsKey("COMPLEX_QUERY_WITH_COMMENTS_AND_BLANKS"));
    }
}
