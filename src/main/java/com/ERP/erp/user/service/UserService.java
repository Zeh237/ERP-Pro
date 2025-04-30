package com.ERP.erp.user.service;

import com.ERP.erp.user.dto.UserUpdateDto;
import com.ERP.erp.user.mapper.UserMapper;
import com.ERP.erp.user.dto.UserDto;
import com.ERP.erp.user.model.Role;
import com.ERP.erp.user.model.User;
import com.ERP.erp.user.repository.RoleRepository;
import com.ERP.erp.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Transactional
    public void registerNewUser(UserDto userDto) {
        logger.info("Starting user registration for username: {}", userDto.getUsername());

        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            logger.error("Username already exists: {}", userDto.getUsername());
            throw new RuntimeException("Username already exists: " + userDto.getUsername());
        }

        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            logger.error("Email already exists: {}", userDto.getEmail());
            throw new RuntimeException("Email already exists: " + userDto.getEmail());
        }

        try {
            User user = userMapper.toEntity(userDto);
            logger.info("User mapped to entity successfully");

            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            logger.info("Password encoded successfully");

            Role role = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> {
                        logger.error("Default role ROLE_USER not found");
                        return new RuntimeException("Default role ROLE_USER not found");
                    });

            user.getRoles().clear();
            user.addRole(role);
            logger.info("Role added to user successfully");

            User savedUser = userRepository.save(user);
            logger.info("User saved successfully with ID: {}", savedUser.getId());
        } catch (Exception e) {
            logger.error("Error during user registration: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to register user: " + e.getMessage(), e);
        }
    }

    public UserDto getUserByName(String username){
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(value -> userMapper.toDto(value)).orElse(null);
    }

    public void updateUser(UserUpdateDto dto) {
        if (dto == null || dto.getId() == null) {
            throw new IllegalArgumentException("User update data cannot be null and must contain an ID");
        }

        userRepository.findById(dto.getId())
                .map(user -> {
                    if (dto.getUsername() != null && !user.getUsername().equals(dto.getUsername())) {
                        if (userRepository.existsByUsername(dto.getUsername())) {
                            throw new IllegalArgumentException("Username already exists");
                        }
                        user.setUsername(dto.getUsername());
                    }

                    if (dto.getEmail() != null && !user.getEmail().equals(dto.getEmail())) {
                        user.setEmail(dto.getEmail());
                    }

                    if (dto.getFirstName() != null && !user.getFirstName().equals(dto.getFirstName())) {
                        user.setFirstName(dto.getFirstName());
                    }

                    if (dto.getLastName() != null && !user.getLastName().equals(dto.getLastName())) {
                        user.setLastName(dto.getLastName());
                    }

                    if (dto.getPassword() != null) {
                        user.setPassword(passwordEncoder.encode(dto.getPassword()));
                    }

                    if (dto.getDepartment() != null && !dto.getDepartment().equals(user.getDepartment())) {
                        user.setDepartment(dto.getDepartment());
                    }

                    if (dto.getJobTitle() != null && !dto.getJobTitle().equals(user.getJobTitle())) {
                        user.setJobTitle(dto.getJobTitle());
                    }

                    if (user.isEnabled() != dto.isEnabled()) {
                        user.setEnabled(dto.isEnabled());
                    }

                    User updatedUser = userRepository.save(user);
                    return userMapper.toDto(updatedUser);
                })
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + dto.getId()));
    }

    public boolean verifyPassword(String username, String rawPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    @Transactional
    public void deactivateUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEnabled(false);
        userRepository.save(user);
    }

}
