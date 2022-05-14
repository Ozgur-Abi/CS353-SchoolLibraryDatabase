package com.app.repository;

import com.app.entity.Event;
import com.app.entity.Finance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@EnableJpaRepositories
public interface FinanceRepository extends JpaRepository<Finance, UUID> {
    List<Finance> findFinancesByValueIsGreaterThan(int value);
    List<Finance> findFinancesByValueIsLessThan(int value);

    Finance findFinanceById(UUID uuid);
}