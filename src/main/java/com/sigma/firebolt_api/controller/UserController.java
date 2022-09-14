package com.sigma.firebolt_api.controller;

import com.sigma.firebolt_api.domain.User;
import com.sigma.firebolt_api.request.InsertUserRequest;
import com.sigma.firebolt_api.request.UpdateUserRequest;
import com.sigma.firebolt_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping("update/{id}")
    public ResponseEntity<Boolean> update(@PathVariable long id, @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.update(request, id));
    }

    @PostMapping("insert")
    public ResponseEntity<Boolean> insert(@RequestBody InsertUserRequest request) {
        return new ResponseEntity<>(userService.insert(request), HttpStatus.CREATED);
    }

    @GetMapping("list/{status}")
    public ResponseEntity<List<User>> listUsers(@PathVariable(required = false) String status) {
        return ResponseEntity.ok(userService.listUsers(status));
    }


}
