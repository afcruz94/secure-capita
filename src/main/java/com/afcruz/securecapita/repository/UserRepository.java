package com.afcruz.securecapita.repository;

import com.afcruz.securecapita.entity.User;

import java.util.Collection;

public interface UserRepository<T extends User> {
    T create(T data);

    T get(long id);

    T update(T data);

    Collection<T> list(int page, int pageSize);

    Boolean delete(Long id);
}