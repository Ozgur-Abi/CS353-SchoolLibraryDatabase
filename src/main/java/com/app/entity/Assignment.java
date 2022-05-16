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

    @ManyToOne
    @JoinColumn(name="instructor_id", referencedColumnName="user_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name="student_id", referencedColumnName="user_id")
    private User receiver;

    @ManyToOne
    @JoinColumn(name="assigned_book_id", referencedColumnName="book_id")
    private Book assigned_book;

    @Column(columnDefinition = "varchar(5000)")
    private String weight;


}
