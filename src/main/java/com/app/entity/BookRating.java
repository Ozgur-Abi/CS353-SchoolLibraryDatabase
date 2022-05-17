package com.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.*;
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
    @Column(name = "rating_id")
    private long id;

    @DateTimeFormat(style = "yyyy-MM-dd")
    private Long rating_date;

    @ManyToOne
    @JoinColumn(name="book_id", referencedColumnName="book_id")
    private Book book;

    @Column(name = "rater_id")
    private long rater_id;



    @Column(columnDefinition = "int(32) CHECK (score > 0)")
    private int score;
    @Column(columnDefinition = "varchar(5000)")
    private String comment;


}
