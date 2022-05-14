package com.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Attendance extends BaseEntity {

    @Column(name = "eventid", columnDefinition = "BINARY(16)")
    private UUID eventID;
    @Column(name = "userid", columnDefinition = "BINARY(16)")
    private UUID userID;
    private boolean attended;
}
