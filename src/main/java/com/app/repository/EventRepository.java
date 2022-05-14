package com.app.repository;

import com.app.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@EnableJpaRepositories
public interface EventRepository extends JpaRepository<Event, UUID> {
    public List<Event> findEventsByName(String name);
    public List<Event> findEventsByClubIDAndStartDateBefore(UUID clubId, Long date);
    public List<Event> findEventsByClubIDAndStartDateAfter(UUID clubId, Long date);
    List<Event> findEventsByStartDateAfter( Long date);
    List<Event> findEventsByStartDateBefore( Long date);

    Event findEventById(UUID id);
}
