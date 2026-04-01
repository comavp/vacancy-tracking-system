package ru.comavp.vacancyscraper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.comavp.vacancyscraper.entity.Experience;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
}
