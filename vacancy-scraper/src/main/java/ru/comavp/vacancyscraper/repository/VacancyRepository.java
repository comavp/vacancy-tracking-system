package ru.comavp.vacancyscraper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.comavp.vacancyscraper.entity.Vacancy;

import java.util.Optional;

public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
    Optional<Vacancy> findByOriginalId(String originalId);
}
