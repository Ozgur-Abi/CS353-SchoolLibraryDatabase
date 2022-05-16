package com.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(User.class)
public class Student implements Serializable {

    @Id
    @OneToOne
    @JoinColumn(name="user_id", referencedColumnName="user_id")
    private User user;

    @Column(columnDefinition = "double(5, 2)")
    private double fines;
    @Column(columnDefinition = "int(32)")
    private int year;
    @Column(columnDefinition = "varchar(50)")
    private String department;


}
