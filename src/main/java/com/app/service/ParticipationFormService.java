package com.app.service;

import com.app.entity.ParticipationForm;
import com.app.repository.ParticipationFormRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired,@NonNull}))
public class ParticipationFormService {
    private final ParticipationFormRepository participationFormRepository;

    public ParticipationForm save(ParticipationForm tag) {
        return participationFormRepository.save(tag);
    }

    public void delete(UUID id) {
        participationFormRepository.deleteById(id);
    }

    public List<ParticipationForm> findAll() {
        return participationFormRepository.findAll();
    }

    public ParticipationForm findOneById( UUID id) {
        return participationFormRepository.findParticipationFormById(id);
    }

    public ParticipationForm findParticipationFormByUserIdAndEventId( UUID userID, UUID eventID) {
        return participationFormRepository.findParticipationFormByUserIdAndEventId(userID, eventID);
    }

    public List<ParticipationForm> findParticipationFormsByEventId(UUID eventID) {
        return participationFormRepository.findParticipationFormsByEventId(eventID);
    }

    public Long count() {
        return participationFormRepository.count();
    }
}
