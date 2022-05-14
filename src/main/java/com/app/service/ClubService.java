package com.app.service;

import com.app.entity.Club;
import com.app.repository.ClubRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.management.OperatingSystemMXBean;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired,@NonNull}))
public class ClubService {
    private final ClubRepository clubRepository;

    public Club save(Club club) {
        return clubRepository.save(club);
    }

    public void delete(UUID id) {
        clubRepository.deleteById(id);
    }

    public List<Club> findAll() {
        return clubRepository.findAll();
    }

    public Club findOneById(UUID id) {
        return clubRepository.findClubById(id);
    }

    public Club findOneByName(String name) {
        return clubRepository.findClubByName(name);
    }

    public Long count() {
        return clubRepository.count();
    }
}
