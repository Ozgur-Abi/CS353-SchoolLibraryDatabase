package com.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
public class BookRequest implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "request_id")
    private long id;

    @Column(columnDefinition = "varchar(500)")
    private String title;
    @Column(columnDefinition = "varchar(500)")
    private String author;
    @Column(columnDefinition = "varchar(500)")
    private String genre;
    @Column(columnDefinition = "int(32)")
    private int published_year;
    @Column(columnDefinition = "varchar(500)")
    private String publisher;
    @Column(columnDefinition = "varchar(500)")
    private String language;
    @Column(columnDefinition = "varchar(500)")
    private String isbn;
    @Column(columnDefinition = "varchar(500)")
    private String issn;

    @DateTimeFormat(style = "yyyy-MM-dd")
    private Long request_date;

    @ManyToOne
    @JoinColumn(name="requester_id", referencedColumnName="user_id")
    private User user;
    
}
