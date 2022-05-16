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
public class ReservationRecord implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "reservation_id")
    private long id;

    @DateTimeFormat(style = "yyyy-MM-dd")
    private Long request_date;

    @ManyToOne
    @JoinColumn(name="book_id", referencedColumnName="book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name="requester_id", referencedColumnName="user_id")
    private User requester;

    @ManyToOne
    @JoinColumn(name="approver_id", referencedColumnName="user_id")
    private User approver;
    
}
