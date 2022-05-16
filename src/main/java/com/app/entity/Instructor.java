package com.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Instructor implements Serializable {

    @Id
    @OneToOne
    @JoinColumn(name="user_id", referencedColumnName="user_id")
    private User user;

    @Column(columnDefinition = "double(5, 2)")
    private double fines;
    @Column(columnDefinition = "varchar(500)")
    private String sections;
    @Column(columnDefinition = "varchar(50)")
    private String department;


}
