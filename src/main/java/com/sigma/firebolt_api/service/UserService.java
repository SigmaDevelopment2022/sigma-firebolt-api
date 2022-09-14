package com.sigma.firebolt_api.service;

import com.sigma.firebolt_api.domain.User;
import com.sigma.firebolt_api.request.InsertUserRequest;
import com.sigma.firebolt_api.request.UpdateUserRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    boolean isBlocked(String username);

    boolean update(UpdateUserRequest request, long id);

    boolean insert(InsertUserRequest request);

    List<User> listUsers(String status);
}
