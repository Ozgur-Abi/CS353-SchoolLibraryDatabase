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

    @ManyToOne
    @JoinColumn(name="sender_id", referencedColumnName="user_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name="receiver_id", referencedColumnName="user_id")
    private User receiver;

    @Column(columnDefinition = "varchar(5000)")
    private String text;


}
