package ru.comavp.vacancyscraper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.comavp.vacancyscraper.entity.Experience;

import java.util.Optional;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    Optional<Experience> findByOriginalId(String originalId);
}
