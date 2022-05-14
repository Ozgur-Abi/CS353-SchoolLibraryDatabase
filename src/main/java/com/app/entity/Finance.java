package com.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.UUID;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Finance extends BaseEntity{

    private String name;
    private String description;
    private int value;
    @Column(name = "clubid", columnDefinition = "BINARY(16)")
    private UUID clubID;

    @DateTimeFormat(style = "yyyy-MM-dd")
    private Long date;

    private String incomeTable;
    private String outcomeTable;
}

