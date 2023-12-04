package com.example.hotdesk.user;

import com.example.hotdesk.common.configuration.CustomPageImpl;
import com.example.hotdesk.user.dto.UserCreateDto;
import com.example.hotdesk.user.dto.UserResponseDto;
import com.example.hotdesk.user.dto.UserUpdateDto;
import com.example.hotdesk.user.entity.Role;
import com.example.hotdesk.user.entity.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {
    @Autowired
    TestRestTemplate testRestTemplate;
    @Autowired
    UserRepository userRepository;
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.0")
            .withUsername("postgres")
            .withPassword("postgres");

    @Test
    @Order(value = 1)
    void createUser() {

        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setName("Ali");
        userCreateDto.setSurname("Ali");
        userCreateDto.setEmail("ali123@gmial.com");
        userCreateDto.setRole(Role.ADMIN);
        userCreateDto.setPassword("123");
        userCreateDto.setPhoneNumber("+998941235476");

        ResponseEntity<UserResponseDto> response = testRestTemplate.postForEntity("/user", userCreateDto, UserResponseDto.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.CREATED);

        UserResponseDto userResponseDto = response.getBody();

        Assertions.assertNotNull(userResponseDto);
        Assertions.assertEquals(userCreateDto.getName(), userResponseDto.getName());
        Assertions.assertEquals(userCreateDto.getSurname(), userResponseDto.getSurname());
        Assertions.assertEquals(userCreateDto.getEmail(), userResponseDto.getEmail());
        Assertions.assertEquals(userCreateDto.getRole(), userResponseDto.getRole());
        Assertions.assertEquals(userCreateDto.getPassword(), userResponseDto.getPassword());
        Assertions.assertEquals(userCreateDto.getPhoneNumber(), userResponseDto.getPhoneNumber());

        Optional<User> optionalUser = userRepository.findById(Integer.valueOf(userResponseDto.getId()));

        Assertions.assertTrue(optionalUser.isPresent());
        User user = optionalUser.get();
        Assertions.assertEquals(user.getPhoneNumber(), userCreateDto.getPhoneNumber());
    }

    @Test
    @Order(value = 2)
    void getUsers() {
        ResponseEntity<CustomPageImpl<UserResponseDto>> response = testRestTemplate.exchange("/user?predicate=name==Ali", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<>() {
        });
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);

        PageImpl<UserResponseDto> body = response.getBody();

        Assertions.assertNotNull(body);
        Assertions.assertEquals(body.getNumberOfElements(), 1);
        UserResponseDto userResponseDto = body.getContent().get(0);

        Assertions.assertEquals(userResponseDto.getName(), "Ali");

    }

    @Test
    @Order(value = 3)
    void getUserById() {
        Optional<User> optionalUser = userRepository.findAll().stream().findAny();

        Assertions.assertTrue(optionalUser.isPresent());
        User user = optionalUser.get();

        ResponseEntity<UserResponseDto> response = testRestTemplate.getForEntity("/user/%s".formatted(user.getId()), UserResponseDto.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @Order(value = 4)
    void updateUser() {
        Optional<User> optionalUser = userRepository.findAll().stream().findAny();

        Assertions.assertTrue(optionalUser.isPresent());
        User user = optionalUser.get();

        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setName("Vali");
        userUpdateDto.setSurname("Valiyev");
        userUpdateDto.setEmail("vali@gmail.com");
        userUpdateDto.setPhoneNumber("98765432");
        userUpdateDto.setRole(Role.WORKER);

        testRestTemplate.put("/user/%s".formatted(user.getId()), userUpdateDto, user.getId());

        ResponseEntity<UserResponseDto> response = testRestTemplate
                .getForEntity("/user/%s".formatted(user.getId()), UserResponseDto.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);

        UserResponseDto updatedUser = response.getBody();
        Assertions.assertNotNull(updatedUser);
    }

    @Test
    @Order(value = 5)
    void deleteUser() {
        Optional<User> optionalUser = userRepository.findAll().stream().findAny();

        Assertions.assertTrue(optionalUser.isPresent());
        User user = optionalUser.get();

        testRestTemplate.delete("/user/%s".formatted(user.getId()));
        ResponseEntity<UserResponseDto> response = testRestTemplate
                .getForEntity("/user/%s".formatted(user.getId()), UserResponseDto.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }
}