package com.app.entity;

import com.app.helpers.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class User implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_id")
    private long id;

    @Column(columnDefinition = "varchar(8)")
    private String bilkentId;
    @Column(columnDefinition = "varchar(100)")
    private String email;
    @Column(columnDefinition = "varchar(100)")
    private String password;
    @Column(columnDefinition = "varchar(20)")
    private String first_name;
    @Column(columnDefinition = "varchar(20)")
    private String last_name;

    private Role role;


/*
    private String username;
    private String description;
    private int bilkent_id;

    private String department;
    @DateTimeFormat(style = "yyyy-MM-dd")
    private Long dateOfBirth;
    private int startOfStudies;
    private String instagramUsername;
    private String linkedinUsername;*/

}
