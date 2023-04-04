package com.remitano.movieapplication.controller;

import com.remitano.movieapplication.MovieApplication;
import com.remitano.movieapplication.model.User;
import com.remitano.movieapplication.model.dto.LoginDTO;
import com.remitano.movieapplication.model.dto.SignUpDTO;
import com.remitano.movieapplication.repository.UserRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = MovieApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {
    private static RestTemplate restTemplate;
    @LocalServerPort
    private int port;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private String baseUrl = "http://localhost";

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void beforeSetup() {
        baseUrl = baseUrl + ":" + port + "/movies/auth";
        User user = new User();
        user.setId(new ObjectId());
        user.setEmail("ydat15@gmail.com");
        user.setPassword(passwordEncoder.encode("12345678"));
        userRepository.save(user);
    }
    @AfterEach
    public void afterSetup() {
        userRepository.deleteAll();

    }
    @Test
    void shouldRegisterUserSuccessful_test() {
        SignUpDTO signUpDto = new SignUpDTO();
        signUpDto.setPassword("12345678");
        signUpDto.setEmail("ydat14@gmail.com");
        ResponseEntity<?> result = restTemplate.postForEntity(baseUrl + "/signup", signUpDto, String.class);

        assertEquals("User registered successfully", result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());

    }
    @Test
    void shouldLoginSuccessful_test() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setPassword("12345678");
        loginDTO.setEmail("ydat15@gmail.com");
        ResponseEntity<String> result = restTemplate.postForEntity(baseUrl + "/signin", loginDTO, String.class);

        assertEquals("User signed-in successfully!.", result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());

    }
}
