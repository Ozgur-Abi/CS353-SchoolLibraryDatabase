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
public class BookRating implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_id")
    private long id;

    @DateTimeFormat(style = "yyyy-MM-dd")
    private Long rating_date;

    @Id
    @Column(name = "book_id")
    private long book_id;

    @Id
    @Column(name = "rater_id")
    private long rater_id;

    @Column(columnDefinition = "int(32)")
    private int score;

    @Column(columnDefinition = "varchar(5000)")
    private String comment;


}
