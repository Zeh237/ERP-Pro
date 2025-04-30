package com.ERP.erp.user.mapper;

import com.ERP.erp.user.dto.UserDto;
import com.ERP.erp.user.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        UserDto dto = new UserDto();
        if (user.getId() != null) dto.setId(user.getId());
        if (user.getUsername() != null) dto.setUsername(user.getUsername());
        if (user.getEmail() != null) dto.setEmail(user.getEmail());
        if (user.getFirstName() != null) dto.setFirstName(user.getFirstName());
        if (user.getLastName() != null) dto.setLastName(user.getLastName());
        if (user.getDepartment() != null) dto.setDepartment(user.getDepartment());
        if (user.getJobTitle() != null) dto.setJobTitle(user.getJobTitle());
        dto.setEnabled(user.isEnabled());
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            dto.setRole(user.getRoles().iterator().next().getId());
        }
        return dto;
    }

    public User toEntity(UserDto dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        if (dto.getUsername() != null) user.setUsername(dto.getUsername());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getFirstName() != null) user.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) user.setLastName(dto.getLastName());
        if (dto.getDepartment() != null) user.setDepartment(dto.getDepartment());
        if (dto.getJobTitle() != null) user.setJobTitle(dto.getJobTitle());
        user.setEnabled(dto.isEnabled());
        if (dto.getPassword() != null) user.setPassword(dto.getPassword());

        return user;
    }
}
