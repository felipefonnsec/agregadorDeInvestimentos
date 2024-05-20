package com.project.agregadorinvestimentos.services;

import com.project.agregadorinvestimentos.dtos.CreateUserDto;
import com.project.agregadorinvestimentos.dtos.UpdateUserDto;
import com.project.agregadorinvestimentos.entity.User;
import com.project.agregadorinvestimentos.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    //injetando os mocks
    @InjectMocks
    private UserService userService;

    //captura o argumento dentro do metodo
    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;
    //argumento de captura para o ID
    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;

    // -- definindo subclass para teste --

    // * teste createUser *
    @Nested
    class createUser{

        @Test
        @DisplayName("Should create a user with success")
        void shoulCreateAUserWithSuccess(){

            // - Arrange -
            //variavel que ira retornar os dados para serem salvos
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "user@teste.com",
                    "1234",
                    Instant.now(),
                    null
            );
            //capturando os dados da entidade que vem do userService ao salvar
            doReturn(user).when(userRepository).save(userArgumentCaptor.capture());
            var input = new CreateUserDto(
                    "username",
                    "email@teste.com",
                    "123");
            // - Act -
            var output = userService.createUser(input);

            // - Assert -
            assertNotNull(output);
            //verificando se o que foi passado esta correto
            var userCaptured = userArgumentCaptor.getValue();
            //verifico se o usuario capturado e o mesmo do input
            assertEquals(input.username(), userCaptured.getUsername());
            assertEquals(input.email(), userCaptured.getEmail());
            assertEquals(input.password(), userCaptured.getPassword());
        }

        //verifica se algum erro esta sendo encapsulado
        @Test
        @DisplayName("Should throw exception when error occurs")
        void shouldThrowExceptionWhenErrorOccurs(){
            //Arrange
            doThrow(new RuntimeException()).when(userRepository).save(any());
            var input = new CreateUserDto(
                    "username",
                    "email@teste.com",
                    "123");
            // - Act & assert -
            assertThrows(RuntimeException.class, () -> userService.createUser(input));
        }
    }

    // * teste by getUserById *
    @Nested
    class getUserById{
        @Test
        @DisplayName("Should get user by id with success when optional is present")
        void shouldGetUserByIdWithSuccessWhenOptionalIsPresent() {
            // - arrange -
            //variavel que ira retornar os dados que estao salvos
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "user@teste.com",
                    "1234",
                    Instant.now(),
                    null
            );

            //capturando o id do usuario
            doReturn(Optional.of(user)).when(userRepository).findById(uuidArgumentCaptor.capture());
            // - act -
            var output = userService.getUserById(user.getUserId().toString());
            // - assert -
            //verifica se o usuario esta presente no retorno do doReturn
            assertTrue(output.isPresent());
            //verifica se o que foi passado no output e o mesmo passado no findById
            assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());
        }

        @Test
        @DisplayName("Should get user by id with success when optional is empty")
        void shouldGetUserByIdWithSuccessWhenOptionalIsEmpty() {
            // - arrange -
            //variavel que ira retornar os dados que estao salvos
            var userById = UUID.randomUUID();

            //capturando o id do usuario
            doReturn(Optional.empty()).when(userRepository).findById(uuidArgumentCaptor.capture());
            // - act -
            var output = userService.getUserById(userById.toString());
            // - assert -
            //verifica se o usuario esta presente no retorno do doReturn
            assertTrue(output.isEmpty());
            //verifica se o que foi passado no output e o mesmo passado no findById
            assertEquals(userById, uuidArgumentCaptor.getValue());
        }
    }

    // * teste listUsers *
    @Nested
    class  listUsers{

        @Test
        @DisplayName("Should return all users whith success")
        void shouldReturnAllUsersWhitSuccess() {
            // - arrange -
            //variavel que ira retornar os dados que estao salvos
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "user@teste.com",
                    "1234",
                    Instant.now(),
                    null
            );

            //capturando a lista usuario
            var userList = List.of(user);
            doReturn(userList).when(userRepository).findAll();
            // - act -
            var output = userService.listUsers();
            // - assert -
            assertNotNull(output);
            assertEquals(userList.size(), output.size());
        }
    }

    // * teste updateUserById *
    @Nested
    class updateUserById{
        @Test
        @DisplayName("Should update user by id when user existe (name and password)")
        void shouldUpdateUserByIdWhenUserExist() {
            // - arrange -
            //usuario de atualizacao
            var updateUserDto = new UpdateUserDto(
                "newUsername",
                "new1234"
            );
            //variavel que ira retornar os dados que estao salvos
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "user@teste.com",
                    "1234",
                    Instant.now(),
                    null
            );

            //capturando o id do usuario
            doReturn(Optional.of(user))
                    .when(userRepository)
                    .findById(uuidArgumentCaptor.capture());
            //capturando o usuario
            doReturn(user)
                    .when(userRepository)
                    .save(userArgumentCaptor.capture());

            // - act -
            userService.updateUserById(user.getUserId().toString(), updateUserDto);

            // - assert -
            assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());
            var userCaptore = userArgumentCaptor.getValue();

            //verificando se o que foi passado no dto é o mesmo do repository no doReturn
            assertEquals(updateUserDto.username(), userCaptore.getUsername());
            assertEquals(updateUserDto.password(), userCaptore.getPassword());
            //verify para ver se os metodos foram chamados
            verify(userRepository, times(1)).findById(uuidArgumentCaptor.getValue());
            verify(userRepository, times(1)).save(user);
        }
        @Test
        @DisplayName("Should not update user by id when user not existe (name and password)")
        void shoulNotdUpdateUserByIdWhenUserNOtExist() {
            // - arrange -
            //usuario de atualizacao
            var updateUserDto = new UpdateUserDto(
                    "newUsername",
                    "new1234"
            );

            //variavel que ira retornar os dados que estao salvos
            var userId = UUID.randomUUID();

            //capturando o id do usuario
            doReturn(Optional.empty())
                    .when(userRepository)
                    .findById(uuidArgumentCaptor.capture());

            // - act -
            userService.updateUserById(userId.toString(), updateUserDto);

            // - assert -
            assertEquals(userId, uuidArgumentCaptor.getValue());
            //verify para ver se os metodos foram chamados
            verify(userRepository, times(1)).findById(uuidArgumentCaptor.getValue());
            verify(userRepository, times(0)).save(any());
        }
    }

    // * teste deleteById *
    @Nested
    class deleteById{

        @Test
        @DisplayName("Sould delete user with success when user exist")
        void shouldDeleteUserWithSuccessWhenUserExist() {
            // - arrange -
            //capturando o id do usuario
            doReturn(true)
                    .when(userRepository)
                    .existsById(uuidArgumentCaptor.capture());
            //quando ha um metodo void tem a possibilidade de indicar para nao fazer nada
            doNothing()
                    .when(userRepository)
                    .deleteById(uuidArgumentCaptor.capture());

            var userId = UUID.randomUUID();
            // - act -
            userService.deleteById(userId.toString());

            // - assert -
            var listId = uuidArgumentCaptor.getAllValues();
            assertEquals(userId, listId.get(0));
            assertEquals(userId, listId.get(1));

            //utilizando verify alem do acertEquals
            verify(userRepository, times(1)).existsById(listId.get(0));
            verify(userRepository, times(1)).deleteById(listId.get(1));
        }

        @Test
        @DisplayName("Sould not delete user with success when user not exist")
        void shouldNotDeleteUserWithSuccessWhenUserNotExist() {
            // - arrange -
            //capturando o id do usuario
            doReturn(false)
                    .when(userRepository)
                    .existsById(uuidArgumentCaptor.capture());

            var userId = UUID.randomUUID();
            // - act -
            userService.deleteById(userId.toString());

            // - assert -
            assertEquals(userId, uuidArgumentCaptor.getValue());

            //utilizando verify alem do acertEquals
            verify(userRepository, times(1)).existsById(uuidArgumentCaptor.getValue());
            verify(userRepository, times(0)).deleteById(any());
        }

    }
}