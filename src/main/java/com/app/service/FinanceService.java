package com.app.service;

import com.app.entity.Event;
import com.app.entity.Finance;
import com.app.repository.EventRepository;
import com.app.repository.FinanceRepository;
import com.app.repository.ParticipationFormRepository;
import com.app.repository.TagRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired,@NonNull}))
public class FinanceService {
    private final FinanceRepository financeRepository;


    public Finance save(Finance finance) {
        return financeRepository.save(finance);
    }

    public void delete(UUID id) {
        financeRepository.deleteById(id);
    }

    public List<Finance> findAll() {
        return financeRepository.findAll();
    }

    public Finance findOneById( UUID id) {
        return financeRepository.findFinanceById(id);
    }

    public List<Finance> findIncomes() {
        return financeRepository.findFinancesByValueIsGreaterThan(0);
    }

    public List<Finance> findOutcomes () {
        return financeRepository.findFinancesByValueIsLessThan(0);
    }

    public Long count() {
        return financeRepository.count();
    }

}