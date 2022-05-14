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
public class ParticipationForm extends BaseEntity{
    @Column(name = "userid", columnDefinition = "BINARY(16)")
    private UUID userId;
    @Column(name = "eventid", columnDefinition = "BINARY(16)")
    private UUID eventId;
    private String question;
    private String answer;

}
