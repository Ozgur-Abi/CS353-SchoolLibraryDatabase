package com.app.entity;

import com.app.helpers.Role;
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
public class ClubRole extends BaseEntity{

    @Column(name = "userid", columnDefinition = "BINARY(16)")
    private UUID userID;
    @Column(name = "clubid", columnDefinition = "BINARY(16)")
    private UUID clubID;
    private Role role;

}
