package com.sigma.firebolt_api.request;

import com.sigma.firebolt_api.domain.UserRole;
import lombok.Builder;
import lombok.Data;

@Data
public class UpdateUserRequest {
    private final String name;
    private final String username;
    private final String password;
    private final UserRole userRole;
    private final boolean blocked;
}
