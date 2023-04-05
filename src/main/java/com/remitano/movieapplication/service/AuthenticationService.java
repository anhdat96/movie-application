package com.remitano.movieapplication.service;

import com.remitano.movieapplication.constans.Constants;
import com.remitano.movieapplication.exception.CustomException;
import com.remitano.movieapplication.model.Role;
import com.remitano.movieapplication.model.User;
import com.remitano.movieapplication.model.dto.SignUpDTO;
import com.remitano.movieapplication.repository.RoleRepository;
import com.remitano.movieapplication.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    public ResponseEntity<?> registerUser(SignUpDTO signUpDto) {
        // check for email exists in DB
        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            throw new CustomException("02");
        }
        User user = new User();
        ObjectId objectId = new ObjectId();
        user.setId(objectId);
        setInformationForUser(signUpDto, user);

        userRepository.save(user);
        return new ResponseEntity<>(Constants.USER_REGISTER_SUCCESS, HttpStatus.OK);
    }

    private void setInformationForUser(SignUpDTO signUpDto, User user) {
        user.setEmail(signUpDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        // set role for user
        Set<Role> roleList = new HashSet<>();
        Role role = new Role();
        ObjectId objectId = new ObjectId();
        role.setId(objectId);
        role.setName(Constants.ROLE_USER);
        roleRepository.save(role);

        roleList.add(role);
        user.setRoles(roleList);
    }
}
