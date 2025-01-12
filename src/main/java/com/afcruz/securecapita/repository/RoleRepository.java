package com.afcruz.securecapita.repository;

import com.afcruz.securecapita.entity.Role;

import java.util.Collection;

public interface RoleRepository<T extends Role> {
    T create(T data);

    T get(long id);

    T update(T data);

    Collection<T> list(int page, int pageSize);

    Boolean delete(Long id);

    void addRoleToUser(Long id, String roleName);

    Role getRoleByUserId(Long usedId);

    Role getRoleByUserEmail(String email);

    void updateUserRole(Long userid, String roleName);
}
