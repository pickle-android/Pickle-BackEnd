package com.pickle.pickle_BE.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;


@Entity
@Table(name = "users")
@Data
@Getter
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "email", length = 50, nullable = false, unique = true)
    private String email;

    @Column(name = "password", length = 60)
    private String password;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @Column(name = "phone_number", length = 11, unique = true)
    private String phoneNumber;

    @Column(name = "register_date", nullable = false)
    private LocalDateTime registerDate;

    @Column(name = "role", nullable = false)
    private byte role;  // ROLE_USER: 0, ROLE_ADMIN: 1

    @Builder
    public User(String name, String email, String password, String phoneNumber) {
        this.userId = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.registerDate = LocalDateTime.now();
    }

    public User update(String name) {
        this.name = name;
        return this;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleName = role == 1 ? "ROLE_ADMIN" : "ROLE_USER";
        return List.of(new SimpleGrantedAuthority(roleName));
    }


    @Override
    public String getUsername() {return email;}
    @Override
    public String getPassword() {return password;}

    @Override
    public boolean isAccountNonExpired() {return true;}

    @Override
    public boolean isAccountNonLocked() {return true;}

    @Override
    public boolean isCredentialsNonExpired() {return true;}

    @Override
    public boolean isEnabled() {return true;}
}