package com.afcruz.securecapita.query;

public class UserQuery {
    public static final String INSERT_USER_QUERY = "INSERT INTO Users (first_name, last_name, email, password, title) VALUES (:firstName, :lastName, :email, :password, :title)";
    public static final String COUNT_USER_EMAIL_QUERY = "SELECT COUNT(*) FROM Users WHERE email = :email";
    public static final String SELECT_USER_BY_ID_QUERY = "SELECT * FROM Users WHERE user_id = :userId";
    public static final String SELECT_ALL_USERS_PAGEABLE_QUERY = "SELECT * FROM Users ORDER BY :sortBy LIMIT :size OFFSET :offset ";
    public static final String DELETE_USER_BY_ID_QUERY = "DELETE FROM Users WHERE user_id = :userId";
    public static final String INSERT_ACCOUNT_VERIFICATION_URL_QUERY = "INSERT INTO AccountVerifications (user_id, url) VALUES (:userId, :url)";
    public static final String DELETE_ACCOUNT_VERIFICATION_URL_QUERY = "DELETE FROM AccountVerifications WHERE user_id = :userId";
}
