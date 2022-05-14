package com.app.repository;

import com.app.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@EnableJpaRepositories
public interface ClubRepository extends JpaRepository<Club, UUID> {
    Club findClubById(UUID uuid);
    Club findClubByName(String name);
}
