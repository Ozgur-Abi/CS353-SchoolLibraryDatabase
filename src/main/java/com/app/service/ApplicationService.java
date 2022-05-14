package com.app.service;

import com.app.entity.Application;
import com.app.repository.ApplicationRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired,@NonNull}))
public class ApplicationService {
    private final ApplicationRepository applicationRepository;

    public Application save(Application application) {
        return applicationRepository.save(application);
    }

    public void delete(UUID id) {
        applicationRepository.deleteById(id);
    }

    public List<Application> findAll() {
        return applicationRepository.findAll();
    }

    public Optional<Application> findOneById(UUID id) {
        return applicationRepository.findById(id);
    }

    public List<Application> findApplicationsByClubID(UUID clubID) {
        return applicationRepository.findApplicationsByClubID(clubID);
    }

    public Application findApplicationBySenderIDAndClubID(UUID senderId, UUID clubId) {
        return applicationRepository.findApplicationBySenderIDAndClubID(senderId, clubId);
    }

    public Long count() {
        return applicationRepository.count();
    }

}
