package com.sigma.firebolt_api.request;

import com.sigma.firebolt_api.domain.UserRole;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class InsertUserRequest {
    private final String name;
    private final String username;
    private final String password;
    private final UserRole userRole;
}
