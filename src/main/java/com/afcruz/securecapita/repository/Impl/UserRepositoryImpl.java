package com.afcruz.securecapita.repository.Impl;

import com.afcruz.securecapita.entity.Role;
import com.afcruz.securecapita.entity.User;
import com.afcruz.securecapita.exception.ApiException;
import com.afcruz.securecapita.repository.RoleRepository;
import com.afcruz.securecapita.repository.UserRepository;
import com.afcruz.securecapita.rowMapper.UserRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
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
            user.setUserId(requireNonNull(holder.getKey()).longValue());
            user.setTitle(role);

            // Add role to the user in UserRoles table
            roleRepository.addRoleToUser(user.getUserId(), ROLE_USER.name());

            // Save verification url
            String verificationURL = getVerificationUrl(UUID.randomUUID().toString(), ACCOUNT.getType());
            namedParameterJdbcTemplate.update(INSERT_ACCOUNT_VERIFICATION_URL_QUERY, of("userId", user.getUserId(), "url", verificationURL));
            // Send email to user with verification url
            // emailService.sendVerificationUrl(user.getFirstName(), user.getEmail(), verificationURL, true);

            // Add two factor verification token
            // twoFactorVerificationService.sendVerificationRequest(user.getUserId());

            user.setEnabled(false);
            user.setIsNonLocked(true);

            return user;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ApiException("Internal error occurred. Contact Support Team");
        }
    }

    @Override
    public User get(Long userId) {
        log.info("Searching user id {}", userId);

        // Search User
        try {
            return namedParameterJdbcTemplate.queryForObject(SELECT_USER_BY_ID_QUERY, of("userId", userId), new UserRowMapper());
        } catch (EmptyResultDataAccessException e) {
            log.warn("No user found with id {}", userId);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return null;
    }

    @Override
    public Collection<User> list(int page, int size, String sort) {
        log.info("Searching all users");
        final int offset = (page * size) - size;

        try {
            return namedParameterJdbcTemplate.query(SELECT_ALL_USERS_PAGEABLE_QUERY, of("size", size, "offset", offset, "sortBy", sort), new UserRowMapper());
        } catch (BadSqlGrammarException ex) {
            log.error("An exception occurred while executing SQL statement: {}", ex.getMessage());
            log.debug("Stack Trace: ", ex);
        } catch (EmptyResultDataAccessException e) {
            log.warn("No users found");
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return List.of();
    }

    @Override
    public User update(User data) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Long userId) {
        log.info("Deleting user {}", userId);

        try {
            // Delete role to the user in UserRoles table
            roleRepository.deleteRoleToUser(userId);

            // Delete user events
            // userEventsRepository.deleteEventsFromUser(userId);

            // Delete two factor verification
            // twoFactorVerificationService.deleteUserVerification(userId);

            // Delete reset password verification
            // resetPasswordRepository.deleteRequestFromUser(userId);

            // Delete Account Verifications
            namedParameterJdbcTemplate.update(DELETE_ACCOUNT_VERIFICATION_URL_QUERY, of("userId", userId));

            // Delete User
            namedParameterJdbcTemplate.update(DELETE_USER_BY_ID_QUERY, of("userId", userId));

            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ApiException("Internal error occurred. Contact Support Team");
        }
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
