package com.sigma.firebolt_api.dao;

import com.sigma.firebolt_api.domain.User;
import com.sigma.firebolt_api.mapper.UserMapper;

import java.util.List;

public interface UserDao {
    User findUsingUsername(String username, UserMapper mapper);

    User findUsingId(long id, UserMapper mapper);

    boolean isBlocked(String username);

    boolean update(User user);

    boolean insert(User user);

    List<User> listUsers(String status);
}
