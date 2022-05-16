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
public class BorrowRecord implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "borrow_id")
    private long id;

    @DateTimeFormat(style = "yyyy-MM-dd")
    private Long borrow_date;
    @DateTimeFormat(style = "yyyy-MM-dd")
    private Long due_date;
    @DateTimeFormat(style = "yyyy-MM-dd")
    private Long return_date;


    @ManyToOne
    @JoinColumn(name="book_id", referencedColumnName="book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name="requester_id", referencedColumnName="user_id")
    private User borrower;

    @ManyToOne
    @JoinColumn(name="approver_id", referencedColumnName="user_id")
    private User approver;
    
}
