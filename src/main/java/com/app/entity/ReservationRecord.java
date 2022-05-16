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
    @Id
    @Column(name = "book_id")
    private long book_id;
    @Id
    @Column(name = "requester_id")
    private long requester_id;
    @Id
    @Column(name = "approver_id")
    private long approver_id;
    
}
