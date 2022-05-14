package com.app.repository;

import com.app.entity.ParticipationForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@EnableJpaRepositories
public interface ParticipationFormRepository extends JpaRepository<ParticipationForm, UUID> {
    public ParticipationForm getById(UUID id);
    public ParticipationForm findParticipationFormByUserIdAndEventId(UUID userID, UUID eventID);
    public List<ParticipationForm> findParticipationFormsByEventId( UUID eventID);

    ParticipationForm findParticipationFormById(UUID uuid);
}
