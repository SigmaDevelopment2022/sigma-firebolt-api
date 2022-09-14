package com.sigma.firebolt_api.mapper;

import com.sigma.firebolt_api.domain.User;
import com.sigma.firebolt_api.domain.UserRole;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface UserMapper extends RowMapper<User> {
    public static class Minimal implements UserMapper {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return User.builder()
                    .id(rs.getLong("id"))
                    .name(rs.getString("name"))
                    .username(rs.getString("username"))
                    .role(UserRole.valueOf(rs.getString("role").toUpperCase()))
                    .blocked(rs.getBoolean("blocked"))
                    .build();
        }
    }

    public static class Full implements UserMapper {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return User.builder()
                    .id(rs.getLong("id"))
                    .name(rs.getString("name"))
                    .username(rs.getString("username"))
                    .password(rs.getString("password"))
                    .role(UserRole.valueOf(rs.getString("role").toUpperCase()))
                    .blocked(rs.getBoolean("blocked"))
                    .created(rs.getString("created"))
                    .build();
        }
    }
}
