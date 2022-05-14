package com.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Event extends BaseEntity{

    private String name;
    @DateTimeFormat(style = "yyyy-MM-dd")
    private Long startDate;
    @DateTimeFormat(style = "yyyy-MM-dd")
    private Long endDate;

    private int fee;
    private int maxParticipants;
    private String description;
    private String location;
    @Column(name = "clubid", columnDefinition = "BINARY(16)")
    private UUID clubID;

    @DateTimeFormat(style = "yyyy-MM-dd")
    private Long applicationDeadline;
}
