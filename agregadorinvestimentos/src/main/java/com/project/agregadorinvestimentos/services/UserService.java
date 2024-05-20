package com.project.agregadorinvestimentos.services;

import com.project.agregadorinvestimentos.dtos.AccountResponseDto;
import com.project.agregadorinvestimentos.dtos.CreateAccountDto;
import com.project.agregadorinvestimentos.dtos.CreateUserDto;
import com.project.agregadorinvestimentos.dtos.UpdateUserDto;
import com.project.agregadorinvestimentos.entity.Account;
import com.project.agregadorinvestimentos.entity.BillingAddress;
import com.project.agregadorinvestimentos.entity.User;
import com.project.agregadorinvestimentos.repositories.AccountRepository;
import com.project.agregadorinvestimentos.repositories.BillingAddressRepository;
import com.project.agregadorinvestimentos.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    BillingAddressRepository billingAddressRepository;

    public UUID createUser(CreateUserDto createUserDto){
        //dto -> entity
        var entity = new User(
                UUID.randomUUID(),
                createUserDto.username(),
                createUserDto.email(),
                createUserDto.password(),
                Instant.now(),
                null,
                new ArrayList<>()
        );
       var userSaved = userRepository.save(entity);
       return userSaved.getUserId();
    }

    public Optional<User> getUserById(String userId) {
       var user = userRepository.findById(UUID.fromString(userId));
       return  user;
    }

    public List<User> listUsers(){
        return userRepository.findAll();
    }

    public void updateUserById(String userId, UpdateUserDto updateUserDto){
        var id = UUID.fromString(userId);
        var userEntity = userRepository.findById(id);

        if (userEntity.isPresent()){
            var user = userEntity.get();

            if (updateUserDto.username() != null){
                user.setUsername(updateUserDto.username());
            }
            if (updateUserDto.password() != null) {
                user.setPassword(updateUserDto.password());
            }
          userRepository.save(user);
        }
    }

    public void deleteById(String userId){
        var id = UUID.fromString(userId);
        var userExist = userRepository.existsById(id);
        if (userExist){
            userRepository.deleteById(id);
        }
    }

    public void createAccount(String userId, CreateAccountDto createAccountDto) {
        var user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        //DTO -> ENTITY
        var account = new Account(
                UUID.randomUUID(),
                user,
                null,
                createAccountDto.description(),
                new ArrayList<>()
        );

        var accountCreated = accountRepository.save(account);

        //criando endereço de pagamento
        var billingAddress = new BillingAddress(
                accountCreated.getAccountId(),
                account,
                createAccountDto.street(),
                createAccountDto.number()
        );

        billingAddressRepository.save(billingAddress);
    }

    public List<AccountResponseDto> listAccounts(String userId) {
        var user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return user.getAccount()
                .stream()
                .map(ac -> new AccountResponseDto(ac.getAccountId().toString(), ac.getDescription()))
                .toList();
    }
}
