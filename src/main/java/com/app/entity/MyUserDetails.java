package com.app.entity;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

public class MyUserDetails implements UserDetails {
    private String email;
    private String password;
    private String username;
    private String description;
    private UUID uuid;

    private String department;
    private int startOfStudies;
    private Long dateOfBirth;
    private String instagramUsername;
    private String linkedinUsername;

    public MyUserDetails(User user) {
        this.uuid = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.username = user.getUsername();
        this.description = user.getDescription();

        this.department = user.getDepartment();
        this.dateOfBirth = user.getDateOfBirth();
        this.startOfStudies = user.getStartOfStudies();
        this.instagramUsername = user.getInstagramUsername();
        this.linkedinUsername = user.getLinkedinUsername();
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

    public String getDescription() {
        return description;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Long getDateOfBirth() {
        return dateOfBirth;
    }

    public int getStartOfStudies() {
        return startOfStudies;
    }

    public String getDepartment() {
        return department;
    }

    public String getInstagramUsername() {
        return instagramUsername;
    }

    public String getLinkedinUsername() {
        return linkedinUsername;
    }

    public UUID getUUID(){return uuid;}

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
