package com.sigma.firebolt_api.repository;

import com.sigma.firebolt_api.dao.UserDao;
import com.sigma.firebolt_api.domain.User;
import com.sigma.firebolt_api.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserDao userDao;

    @Override
    public User findUsingId(String username, UserMapper mapper) {
        return userDao.findUsingUsername(username, mapper);
    }

    @Override
    public User findUsingId(long id, UserMapper mapper) {
        return userDao.findUsingId(id, mapper);
    }

    @Override
    public boolean isBlocked(String username) {
        return userDao.isBlocked(username);
    }

    @Override
    public boolean update(User user) {
        return userDao.update(user);
    }

    @Override
    public boolean insert(User user) {
        return userDao.insert(user);
    }

    @Override
    public List<User> listUsers(String status) {
        return userDao.listUsers(status);
    }
}
