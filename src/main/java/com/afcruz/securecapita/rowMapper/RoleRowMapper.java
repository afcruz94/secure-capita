package com.afcruz.securecapita.rowMapper;

import com.afcruz.securecapita.entity.Role;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleRowMapper implements RowMapper<Role> {
    @Override
    public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Role.builder()
                .roleId(rs.getLong("role_id"))
                .name(rs.getString("name"))
                .permission(rs.getString("permission"))
                .build();
    }
}
