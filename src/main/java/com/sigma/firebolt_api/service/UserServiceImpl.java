package com.sigma.firebolt_api.service;

import com.sigma.firebolt_api.domain.User;
import com.sigma.firebolt_api.domain.UserRole;
import com.sigma.firebolt_api.mapper.UserMapper;
import com.sigma.firebolt_api.repository.UserRepository;
import com.sigma.firebolt_api.request.InsertUserRequest;
import com.sigma.firebolt_api.request.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUsingId(username, new UserMapper.Full());
        return Optional
                .ofNullable(user)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }

    @Override
    public boolean isBlocked(String username) {
        return userRepository.isBlocked(username);
    }

    @Override
    public boolean update(UpdateUserRequest request, long id) {
        User baseUser = userRepository.findUsingId(id, new UserMapper.Full());

        if (request.getName() != null) {
            baseUser.setName(request.getName());
        }

        if (request.getUsername() != null) {
            baseUser.setUsername(request.getUsername());
        }

        if (request.getPassword() != null) {
            baseUser.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getUserRole() != null) {
            baseUser.setRole(request.getUserRole());
        }

        if (request.isBlocked() != baseUser.isBlocked()) {
            baseUser.setBlocked(request.isBlocked());
        }

        return userRepository.update(baseUser);
    }

    @Override
    public boolean insert(InsertUserRequest request) {
        return userRepository.insert(User.builder()
                .name(request.getName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getUserRole())
                .build());
    }

    @Override
    public List<User> listUsers(String status) {
        return userRepository.listUsers(status);
    }
}
