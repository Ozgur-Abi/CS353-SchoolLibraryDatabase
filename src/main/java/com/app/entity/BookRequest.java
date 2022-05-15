package com.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BookRequest extends BaseEntity{

    @DateTimeFormat(style = "yyyy-MM-dd")
    private Long request_date;
    @Id
    @Column(name = "book_id", columnDefinition = "BINARY(16)")
    private UUID book_id;
    @Id
    @Column(name = "requester_id", columnDefinition = "BINARY(16)")
    private UUID requester_id;
    @Id
    @Column(name = "approver_id", columnDefinition = "BINARY(16)")
    private UUID approver_id;
    
}
