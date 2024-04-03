package com.oftwapi.entity;

import com.oftwapi.dto.request.auth.SignUpRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name="user")
@Table(name="user")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    private String username;
    private String password;
    private String email;
    private String type;
    private String role;

    public UserEntity (SignUpRequestDto dto) {
        this.username = dto.getUsername();
        this.email = dto.getEmail();
        this.password = dto.getPassword();
        this.type = "app";
        this.role = "ROLE_USER";
    }
}
