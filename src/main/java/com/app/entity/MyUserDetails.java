package com.app.entity;

import com.app.helpers.Role;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

public class MyUserDetails implements UserDetails {
    private String email;
    private String password;
    private String bilkentId;
    private String firstName;
    private String lastName;

    private Role role;
    private long id;


    public MyUserDetails(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.bilkentId = user.getBilkentId();
        this.firstName = user.getFirst_name();
        this.lastName = user.getLast_name();
        this.role = user.getRole();
    }

    public MyUserDetails(){}


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return bilkentId;
    }

    public String getEmail() {
        return email;
    }

    public String getBilkentId() {
        return bilkentId;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }

    public Role getRole() {
        return role;
    }


    public long getId(){return id;}

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
