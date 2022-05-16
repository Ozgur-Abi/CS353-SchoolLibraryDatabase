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
public class Notification implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "notification_id")
    private long id;

    @DateTimeFormat(style = "yyyy-MM-dd")
    private Long notification_date;

    @Id
    @Column(name = "sender_id")
    private long sender_id;

    @Id
    @Column(name = "receiver_id")
    private long receiver_id;

    @Column(columnDefinition = "varchar(5000)")
    private String text;


}
