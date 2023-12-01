package com.example.hotdesk.user.dto;

import com.example.hotdesk.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserUpdateDto {
    private String name;
    private String surname;
    private String phoneNumber;
    private String email;
    private Role role;
}
