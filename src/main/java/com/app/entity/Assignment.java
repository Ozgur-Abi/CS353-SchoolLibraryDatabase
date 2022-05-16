package com.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Assignment implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "assignment_id")
    private long id;

    @DateTimeFormat(style = "yyyy-MM-dd")
    private Long due_date;

    @Id
    @Column(name = "instructor_id")
    private long sender_id;

    @Id
    @Column(name = "student_id")
    private long receiver_id;

    @Id
    @Column(name = "assignmed_book_id")
    private long assigned_book_id;

    @Column(columnDefinition = "varchar(5000)")
    private String weight;


}
