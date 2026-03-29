package ru.comavp.vacancyscraper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.comavp.vacancyscraper.entity.Employer;

import java.util.Optional;

public interface EmployerRepository extends JpaRepository<Employer, Long> {
    Optional<Employer> findByOriginalId(String originalId);
}
