package com.afcruz.securecapita.query;

public class RoleQuery {
    public final static String INSERT_ROLE_TO_USER = "INSERT INTO UserRoles (user_id, role_id) VALUES (:userId, :roleId)";
    public final static String SELECT_ROLE_BY_NAME_QUERY = "SELECT * FROM Roles WHERE name = :name";
}
