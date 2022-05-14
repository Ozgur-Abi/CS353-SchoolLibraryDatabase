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
public class Application extends BaseEntity{

    @Column(name = "senderid", columnDefinition = "BINARY(16)")
    private UUID senderID;

    private String applicationStatus;
    @Column(name = "clubid", columnDefinition = "BINARY(16)")
    private UUID clubID;

}
