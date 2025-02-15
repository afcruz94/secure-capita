package com.afcruz.securecapita.controller;

import com.afcruz.securecapita.Dto.PageableDTO;
import com.afcruz.securecapita.Dto.UserDTO;
import com.afcruz.securecapita.responseHandler.HttpResponse;
import com.afcruz.securecapita.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static java.time.Instant.now;
import static java.util.Map.of;

@RestController
@RequestMapping(path = "/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<HttpResponse> registerUser(@RequestBody @Valid UserDTO userRequest) {
        final UserDTO userDTO = userService.createUser(userRequest);

        return ResponseEntity.created(this.getURI()).body(
                HttpResponse.builder()
                        .timestamp(now())
                        .data(of("user", userDTO))
                        .reason("User created")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<HttpResponse> findUserById(@PathVariable(name = "id") @Min(1) Long userId) {
        final UserDTO userDTO = userService.getUserById(userId);

        return ResponseEntity.ok()
                .body(this.validateAndCreateRequestResponse(userDTO, userId));
    }

    @GetMapping("find-all")
    public ResponseEntity<HttpResponse> findAllUsers(@RequestParam() Integer page,
                                                     @RequestParam(defaultValue = "10", required = false) Integer size,
                                                     @RequestParam(defaultValue = "user_id", required = false) String sortBy) {
        final List<UserDTO> usersList = userService.getAllUsers(page, size, sortBy);
        String reason = "List of users";
        HttpStatus status = HttpStatus.OK;

        if (usersList.isEmpty()) {
            reason = "No users found";
            status = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now())
                        .data(new PageableDTO(page, size, sortBy, usersList))
                        .reason(reason)
                        .status(status)
                        .statusCode(status.value())
                        .build());
    }

    @PutMapping("update-user/{userId}")
    public ResponseEntity<HttpResponse> updateUser(@RequestBody @Valid UserDTO userUpdated, @PathVariable Long userId) {
        final UserDTO userDTO = userService.updateUser(userUpdated, userId);

        return ResponseEntity.ok()
                .body(this.validateAndCreateRequestResponse(userDTO, userId));
    }

    @PatchMapping("update-user-role/{userId}/{role}")
    public ResponseEntity<HttpResponse> updateUserRole(@PathVariable String role, @PathVariable Long userId) {
        final UserDTO userDTO = userService.updateUserRole(userId, role);

        return ResponseEntity.ok()
                .body(this.validateAndCreateRequestResponse(userDTO, userId));
    }

    @DeleteMapping("delete-user/{id}")
    public ResponseEntity<HttpResponse> deleteUserById(@PathVariable(name = "id") @Min(1) Long userId) {
        final boolean isDeleted = userService.deleteUser(userId);
        final HttpStatus status = isDeleted ? HttpStatus.OK : HttpStatus.NOT_FOUND;

        return ResponseEntity.status(status).build();
    }

    private URI getURI() {
        return URI.create(ServletUriComponentsBuilder.fromCurrentRequest().toUriString());
    }

    private HttpResponse validateAndCreateRequestResponse(UserDTO userDTO, Long userId) {
        Object data = userDTO;
        String reason = "User found";
        HttpStatus status = HttpStatus.OK;
        int statusValue = HttpStatus.OK.value();

        if (userDTO == null) {
            data = "User with id [" + userId + "] not found!";
            reason = "User not found";
            status = HttpStatus.NOT_FOUND;
            statusValue = status.value();
        }

        return HttpResponse.builder()
                .timestamp(now())
                .data(of("user", data))
                .reason(reason)
                .status(status)
                .statusCode(statusValue)
                .build();
    }
}
