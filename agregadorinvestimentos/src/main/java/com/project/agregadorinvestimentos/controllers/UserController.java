package com.project.agregadorinvestimentos.controllers;

import com.project.agregadorinvestimentos.dtos.AccountResponseDto;
import com.project.agregadorinvestimentos.dtos.CreateAccountDto;
import com.project.agregadorinvestimentos.dtos.CreateUserDto;
import com.project.agregadorinvestimentos.dtos.UpdateUserDto;
import com.project.agregadorinvestimentos.entity.User;
import com.project.agregadorinvestimentos.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserDto createUserDto){
        var userIs = userService.createUser(createUserDto);
        return ResponseEntity.created(URI.create("/v1/users/" + userIs.toString())).build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUsersById(@PathVariable("userId") String userId) {
        var user = userService.getUserById(userId);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        /* Codigo acima resume o codigo abaixo
        if (user.isPresent()){
            return ResponseEntity.ok(user.get());
            } else {
            return ResponseEntity.notFound().build();
            }
        * */
    }

    @GetMapping
    public ResponseEntity<List<User>> listUsers(){
        var users = userService.listUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUserById(@PathVariable("userId") String userId, @RequestBody UpdateUserDto updateUserDto){
        userService.updateUserById(userId, updateUserDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteById(@PathVariable("userId") String userId){
        userService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }

    // - criacao da conta do usuario -

    @PostMapping("/{userId}/accounts")
    public ResponseEntity<Void> accountUser(@PathVariable("userId") String userId,
                                            @RequestBody CreateAccountDto createAccountDto){
        userService.createAccount(userId, createAccountDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/accounts")
    public ResponseEntity<List<AccountResponseDto>> listAccounts (@PathVariable("userId") String userId){
        var accounts = userService.listAccounts(userId);
        return ResponseEntity.ok(accounts);
    }



}
