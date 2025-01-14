package com.afcruz.securecapita.controller;

import com.afcruz.securecapita.Dto.PageableDTO;
import com.afcruz.securecapita.Dto.UserDTO;
import com.afcruz.securecapita.entity.User;
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
@RequestMapping(path = "/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<HttpResponse> registerUser(@RequestBody @Valid User user) {
        final UserDTO userDTO = userService.createUser(user);

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
    public ResponseEntity<HttpResponse> findUserById(@PathVariable(name = "id") @Min(1) Integer userId) {
        final UserDTO userDTO = userService.getUserById(userId);

        final Object data = userDTO != null ? userDTO : "User with id [" + userId + "] not found!";
        final String reason = userDTO != null ? "User found" : "User not found";

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now())
                        .data(of("user", data))
                        .reason(reason)
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("findAll")
    public ResponseEntity<HttpResponse> findAllUsers(@RequestParam() Integer page,
                                                     @RequestParam(defaultValue = "10", required = false) Integer size,
                                                     @RequestParam(defaultValue = "user_id", required = false) String sortBy) {
        final List<UserDTO> usersList = userService.getAllUsers(page, size, sortBy);
        final String reason = usersList.isEmpty() ? "No users found" : "List of users";

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(now())
                        .data(new PageableDTO(page, size, sortBy, usersList))
                        .reason(reason)
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    private URI getURI() {
        return URI.create(ServletUriComponentsBuilder.fromCurrentRequest().toUriString());
    }
}
