# ParseSql4J
Parse a file and associate SQL queries to a map. Useful for separating SQL from code logic.

Inspired from [nleof/goyesql](https://github.com/nleof/goyesql) and [knadh/goyesql](https://github.com/knadh/goyesql/tree/master)

# Usage

Create a file containing your SQL queries. E.g queries.sql
```sql
---- name: GET_ALL_USERS ----
SELECT * FROM users;

---- name: GET_ALL_USERS_WITH_NAME ----
SELECT * FROM users where name = 'Mike';

---- name: GET_ALL_USERS_WITH_ID ----
SELECT * FROM users where name = 'Mike' and id > 53;
```

Code to get Query Map
```java
Map<String, String> queryMap = SqlParser.parse("queries.sql");
String query = queryMap.get("GET_ALL_USERS");
```

Code to get Query Map via input stream
```java
import java.io.InputStream;

InputStream is = // code to get object ..... 
Map<String, String> queryMap = SqlParser.parse(is);
String query = queryMap.get("GET_ALL_USERS");
```

Enjoy!