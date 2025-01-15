package com.afcruz.securecapita.repository;

import com.afcruz.securecapita.entity.Role;

import java.util.Collection;

public interface RoleRepository<T extends Role> {
    T get(long roleId);

    Collection<T> list(int page, int pageSize);

    T create(T data);

    T update(T data);

    Boolean delete(Long roleId);

    Role getRoleByUserId(Long userId);

    Role getRoleByUserEmail(String email);

    void addRoleToUser(Long userId, String roleName);

    void updateUserRole(Long userId, String roleName);

    void deleteRoleToUser(Long userId);
}
