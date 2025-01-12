package com.afcruz.securecapita.repository.Impl;

import com.afcruz.securecapita.entity.Role;
import com.afcruz.securecapita.entity.User;
import com.afcruz.securecapita.exception.ApiException;
import com.afcruz.securecapita.repository.RoleRepository;
import com.afcruz.securecapita.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static com.afcruz.securecapita.enums.RoleType.ROLE_USER;
import static com.afcruz.securecapita.enums.VerificationType.ACCOUNT;
import static com.afcruz.securecapita.query.UserQuery.*;
import static java.util.Map.of;
import static java.util.Objects.requireNonNull;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepository<User> {
    private final RoleRepository<Role> roleRepository;
    private final BCryptPasswordEncoder encoder;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User create(User user) {
        // Check email is unique
        if (getEmailCount(user.getEmail().trim().toLowerCase()) > 0) {
            throw new ApiException("Email already in use. Please use a different email and try again");
        }

        // Save new user
        try {
            final String role = ROLE_USER.name().substring(5);

            log.info("Adding new user {} with role {}", user.getEmail(), role);

            KeyHolder holder = new GeneratedKeyHolder();
            // Parameters Source
            SqlParameterSource parameterSource = getSqlParameterSource(user, role);
            // Insert User
            namedParameterJdbcTemplate.update(INSERT_USER_QUERY, parameterSource, holder);
            // Set User Id
            user.setUserId(requireNonNull(holder.getKey()).longValue());
            // Set role in use, ignoring prefix
            user.setTitle(role);
            // Add role to the user in UserRoles table
            roleRepository.addRoleToUser(user.getUserId(), ROLE_USER.name());
            // Send verification url
            String verificationURL = getVerificationUrl(UUID.randomUUID().toString(), ACCOUNT.getType());
            // Save verification url
            namedParameterJdbcTemplate.update(INSERT_ACCOUNT_VERIFICATION_URL_QUERY, of("userId", user.getUserId(), "url", verificationURL));
            // Send email to user with verification url
            // emailService.sendVerificationUrl(user.getFirstName(), user.getEmail(), verificationURL, true);
            user.setEnabled(false);
            user.setIsNonLocked(true);
            // Return created user
            return user;
            // If any errors, throw exception with proper message
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ApiException("Internal error occurred. Contact Support Team");
        }
    }

    @Override
    public User get(long id) {
        // Search User
        // If no user for given id, throw
        // If any error, thrown exception
        return null;
    }

    @Override
    public User update(User data) {
        return null;
    }

    @Override
    public Collection<User> list(int page, int pageSize) {
        return List.of();
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }

    private Integer getEmailCount(String email) {
        return namedParameterJdbcTemplate.queryForObject(COUNT_USER_EMAIL_QUERY, of("email", email), Integer.class);
    }

    private SqlParameterSource getSqlParameterSource(User user, String role) {
        return new MapSqlParameterSource()
                .addValue("firstName", user.getFirstName())
                .addValue("lastName", user.getLastName())
                .addValue("email", user.getEmail())
                .addValue("password", encoder.encode(user.getPassword()))
                .addValue("title", role);
    }

    private String getVerificationUrl(String key, String type) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/verify/" + type + "/" + key).toUriString();
    }
}
