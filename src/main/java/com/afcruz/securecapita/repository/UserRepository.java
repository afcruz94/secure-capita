package com.afcruz.securecapita.repository;

import com.afcruz.securecapita.entity.User;

import java.util.Collection;

public interface UserRepository<T extends User> {
    T create(T data);

    T get(long userId);

    T update(T data);

    Collection<T> list(int page, int size, String sort);

    Boolean delete(Long id);
}