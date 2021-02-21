package com.epam.esm.repository;

import com.epam.esm.model.User;

import java.util.List;

public interface UserRepository {

    List<User> getUsers(int limit, int offset);

    User getUserById(long id);

    long getCount();
}
