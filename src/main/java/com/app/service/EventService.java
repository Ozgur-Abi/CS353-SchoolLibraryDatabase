package com.app.service;

import com.app.entity.Event;
import com.app.repository.EventRepository;
import com.app.repository.ParticipationFormRepository;
import com.app.repository.TagRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired,@NonNull}))
public class EventService {
    private final EventRepository eventRepository;
    private final TagRepository tagRepository;
    private final ParticipationFormRepository participationFormRepository;

    //event service
    public Event save(Event event) {
        return eventRepository.save(event);
    }

    public void delete(UUID id) {
        eventRepository.deleteById(id);
    }

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    public Event findOneById( UUID id) {
        return eventRepository.findEventById(id);
    }

    public List<Event> findEventByName(String name) {
        return eventRepository.findEventsByName(name);
    }

    public List<Event> findEventsByClubIDAndDateBefore(UUID clubId) {
        return eventRepository.findEventsByClubIDAndStartDateBefore(clubId,
                Long.parseLong((new java.sql.Date(System.currentTimeMillis()).toString()).replace("-", "")) );
    }

    public List<Event> findEventsByClubIDAndDateAfter(UUID clubId) {
        return eventRepository.findEventsByClubIDAndStartDateAfter(clubId,
                Long.parseLong((new java.sql.Date(System.currentTimeMillis()).toString()).replace("-", "")) );
    }

    public List<Event> findEventsByStartDateAfter() {
        return eventRepository.findEventsByStartDateAfter(
                Long.parseLong((new java.sql.Date(System.currentTimeMillis()).toString()).replace("-", ""))
        );
    }

    public List<Event> findEventsByStartDateBefore() {
        return eventRepository.findEventsByStartDateBefore(
                Long.parseLong((new java.sql.Date(System.currentTimeMillis()).toString()).replace("-", ""))
        );
    }

    public Long count() {
        return eventRepository.count();
    }

    //Tag and ParticipationForm methods could be added
}
