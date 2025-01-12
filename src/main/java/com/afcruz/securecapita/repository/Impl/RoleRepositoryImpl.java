package com.afcruz.securecapita.repository.Impl;

import com.afcruz.securecapita.entity.Role;
import com.afcruz.securecapita.exception.ApiException;
import com.afcruz.securecapita.repository.RoleRepository;
import com.afcruz.securecapita.rowMapper.RoleRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

import static com.afcruz.securecapita.enums.RoleType.ROLE_USER;
import static com.afcruz.securecapita.query.RoleQuery.INSERT_ROLE_TO_USER;
import static com.afcruz.securecapita.query.RoleQuery.SELECT_ROLE_BY_NAME_QUERY;
import static java.util.Map.of;
import static java.util.Objects.requireNonNull;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RoleRepositoryImpl implements RoleRepository<Role> {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Role create(Role data) {
        return null;
    }

    @Override
    public Role get(long id) {
        return null;
    }

    @Override
    public Role update(Role data) {
        return null;
    }

    @Override
    public Collection<Role> list(int page, int pageSize) {
        return List.of();
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }

    @Override
    public void addRoleToUser(Long userId, String roleName) {
        log.info("Adding role {} to user id : {}", roleName, userId);

        try {
            Role role = namedParameterJdbcTemplate.queryForObject(SELECT_ROLE_BY_NAME_QUERY, of("name", roleName), new RoleRowMapper());
            namedParameterJdbcTemplate.update(INSERT_ROLE_TO_USER, of("userId", userId, "roleId", requireNonNull(role).getRoleId()));
        } catch (EmptyResultDataAccessException e) {
            throw new ApiException("No role found by name: " + ROLE_USER.name());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ApiException("An internal error occurred when add role " + roleName
                    + " to the user " + userId
                    + ". Contact Support Team");
        }
    }

    @Override
    public Role getRoleByUserId(Long usedId) {
        return null;
    }

    @Override
    public Role getRoleByUserEmail(String email) {
        return null;
    }

    @Override
    public void updateUserRole(Long userid, String roleName) {

    }
}
