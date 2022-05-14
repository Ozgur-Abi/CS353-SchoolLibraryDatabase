package com.app.service;

import com.app.entity.Tag;
import com.app.repository.TagRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired,@NonNull}))
public class TagService {
    private final TagRepository tagRepository;

    public Tag save(Tag tag) {
        return tagRepository.save(tag);
    }

    public void delete(UUID id) {
        tagRepository.deleteById(id);
    }

    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    public Tag findOneById( UUID id) {
        return tagRepository.findTagById(id);
    }

    public List<Tag> findTagByEventID(UUID eventID) {
        return tagRepository.findTagByEventID(eventID);
    }

    public Long count() {
        return tagRepository.count();
    }
}
