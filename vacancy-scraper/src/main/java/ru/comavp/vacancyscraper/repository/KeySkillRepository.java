package ru.comavp.vacancyscraper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.comavp.vacancyscraper.entity.KeySkill;

import java.util.Optional;

public interface KeySkillRepository extends JpaRepository<KeySkill, Long> {
    Optional<KeySkill> findByName(String name);
}
