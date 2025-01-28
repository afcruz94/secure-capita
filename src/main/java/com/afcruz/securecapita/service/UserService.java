package com.afcruz.securecapita.service;

import com.afcruz.securecapita.Dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO getUserById(Long userId);

    List<UserDTO> getAllUsers(int page, int size, String sort);

    UserDTO createUser(UserDTO userRequest);

    UserDTO updateUser(UserDTO userUpdated, Long userId);

    UserDTO updateUserRole(Long userId, String role);

    Boolean deleteUser(Long userId);
}
