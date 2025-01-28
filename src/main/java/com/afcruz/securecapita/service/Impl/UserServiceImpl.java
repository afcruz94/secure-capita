package com.afcruz.securecapita.service.Impl;

import com.afcruz.securecapita.Dto.UserDTO;
import com.afcruz.securecapita.dtoMapper.UserDTOMapper;
import com.afcruz.securecapita.entity.User;
import com.afcruz.securecapita.repository.UserRepository;
import com.afcruz.securecapita.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository<User> userRepository;

    @Override
    public UserDTO getUserById(Long userId) {
        User user = userRepository.get(userId);

        if (user != null) {
            return UserDTOMapper.fromUser(user);
        }

        return null;
    }

    @Override
    public List<UserDTO> getAllUsers(int page, int size, String sort) {
        return userRepository.list(page, size, sort)
                .stream()
                .map(UserDTOMapper::fromUser)
                .toList();
    }

    @Override
    public UserDTO createUser(UserDTO userRequest) {
        User user = UserDTOMapper.toUser(userRequest);

        return UserDTOMapper.fromUser(userRepository.create(user));
    }

    @Override
    public UserDTO updateUser(UserDTO userUpdated, Long userId) {
        User user = UserDTOMapper.toUser(userUpdated);

        Object updatedUser = userRepository.update(user, userId);

        if (updatedUser != null) return UserDTOMapper.fromUser((User) updatedUser);
        else return null;
    }

    @Override
    public UserDTO updateUserRole(Long userId, String role) {
        Object updatedUser = userRepository.updateUserRole(userId, role);

        if (updatedUser != null) return UserDTOMapper.fromUser((User) updatedUser);
        else return null;
    }

    @Override
    public Boolean deleteUser(Long userId) {
        return userRepository.delete(userId);
    }
}
