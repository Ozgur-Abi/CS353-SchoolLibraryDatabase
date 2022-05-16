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
public class Librarian implements Serializable {

    @Id
    @OneToOne
    @JoinColumn(name="user_id", referencedColumnName="user_id")
    private User user;

    @Column(columnDefinition = "int(32)")
    private int years_of_experience;
}
