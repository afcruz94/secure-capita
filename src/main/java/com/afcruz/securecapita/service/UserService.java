package com.afcruz.securecapita.service;

import com.afcruz.securecapita.Dto.UserDTO;
import com.afcruz.securecapita.entity.User;

import java.util.List;

public interface UserService {
    UserDTO getUserById(Long userId);

    List<UserDTO> getAllUsers(int page, int size, String sort);

    UserDTO createUser(User user);

    UserDTO updateUser(User user);

    Boolean deleteUser(Long userId);
}
