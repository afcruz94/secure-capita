package com.afcruz.securecapita.rowMapper;

import com.afcruz.securecapita.entity.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .userId(rs.getLong("user_id"))
                .firstName(rs.getString("first_name"))
                .lastName(rs.getString("last_name"))
                .email(rs.getString("email"))
                .address(rs.getString("address"))
                .phone(rs.getString("phone"))
                .title(rs.getString("title"))
                .bio(rs.getString("bio"))
                .enabled(rs.getBoolean("enabled"))
                .isNonLocked(rs.getBoolean("non_locked"))
                .createAt(LocalDateTime.of(rs.getDate("created_at").toLocalDate(), rs.getTime("created_at").toLocalTime()))
                .build();
    }
}
