package com.afcruz.securecapita.repository;

import com.afcruz.securecapita.entity.User;

import java.util.Collection;

public interface UserRepository<T extends User> {
    T get(Long userId);

    Collection<T> list(int page, int size, String sort);

    T create(T data);

    T update(T data);

    Boolean delete(Long userId);
}