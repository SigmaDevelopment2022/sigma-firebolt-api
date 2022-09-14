package com.sigma.firebolt_api.dao;

import com.sigma.firebolt_api.domain.User;
import com.sigma.firebolt_api.exception.DuplicateResourceException;
import com.sigma.firebolt_api.exception.ResourceNotFoundException;
import com.sigma.firebolt_api.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class UserDaoImpl implements UserDao {
    private final JdbcTemplate mariadb;

    @Override
    public User findUsingUsername(String username, UserMapper mapper) {
        String statement = "select * from sgm_users where username=?";
        try {
            return mariadb.queryForObject(statement, mapper, username);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public User findUsingId(long id, UserMapper mapper) {
        String statement = "select * from sgm_users where id=?";
        try {
            return mariadb.queryForObject(statement, mapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Usuário não encontrado");
        }
    }

    @Override
    public boolean isBlocked(String username) {
        String statement = "select blocked from sgm_users where username=?";
        try {
            return Boolean.TRUE.equals(mariadb.queryForObject(statement, Boolean.class, username));
        } catch (EmptyResultDataAccessException e) {
            return true;
        }
    }

    @Override
    public boolean update(User user) {
        String statement = "update sgm_users set name=?, username=?, password=?, role=?, blocked=? where id=?";
        try {
            return mariadb.update(statement,
                    user.getName(),
                    user.getUsername(),
                    user.getPassword(),
                    user.getRole().name().toUpperCase(),
                    user.isBlocked(),
                    user.getId()) > 0;
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Usuário não encontrado: " + user.getUsername());
        } catch (DataAccessException e) {
            if (e.getLocalizedMessage().toLowerCase().contains("duplicate entry")) {
                throw new DuplicateResourceException("Esse nome de usuário ja está em uso: " + user.getUsername());
            }
            throw e;
        }
    }

    @Override
    public boolean insert(User user) {
        String statement = "insert into sgm_users(name, username, password, role) values (?,?,?,?)";
        try {
            return mariadb.update(statement,
                    user.getName(),
                    user.getUsername(),
                    user.getPassword(),
                    user.getRole().name().toUpperCase()) > 0;
        } catch (DataAccessException e) {
            if (e.getLocalizedMessage().toLowerCase().contains("duplicate entry")) {
                throw new DuplicateResourceException("Esse nome de usuário ja está em uso: " + user.getUsername());
            }
            throw e;
        }
    }

    @Override
    public List<User> listUsers(String status) {
        String whereClause = "";

        if (status.equalsIgnoreCase("blocked") || status.equalsIgnoreCase("unblocked")) {
            whereClause += " where blocked = " + status.equalsIgnoreCase("blocked");
        }


        String statement = "select * from sgm_users" + whereClause;
        return mariadb.query(statement, new UserMapper.Minimal());
    }
}
