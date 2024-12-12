---- name: GET_ALL_USERS ----
SELECT * FROM users;

---- name: GET_ALL_USERS_WITH_NAME ----
SELECT * FROM users where name = 'Mike';

---- name: GET_ALL_USERS_WITH_ID ----
SELECT * FROM users where name = 'Mike' and id > 53;