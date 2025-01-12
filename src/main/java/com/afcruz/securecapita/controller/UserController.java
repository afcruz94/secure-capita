package com.afcruz.securecapita.controller;

import com.afcruz.securecapita.Dto.UserDTO;
import com.afcruz.securecapita.entity.User;
import com.afcruz.securecapita.responseHandler.HttpResponse;
import com.afcruz.securecapita.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static java.time.Instant.now;
import static java.util.Map.of;

@RestController
@RequestMapping(path = "/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<HttpResponse> registerUser(@RequestBody @Valid User user) {
        UserDTO userDTO = userService.createUser(user);

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

    private URI getURI() {
        return URI.create(ServletUriComponentsBuilder.fromCurrentRequest().toUriString());
    }
}
