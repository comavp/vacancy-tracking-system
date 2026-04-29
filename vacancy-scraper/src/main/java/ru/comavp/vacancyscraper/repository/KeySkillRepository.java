package ru.comavp.vacancyscraper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.comavp.vacancyscraper.entity.KeySkill;
import ru.comavp.vacancyscraper.entity.KeySkillCount;

import java.util.List;
import java.util.Optional;

public interface KeySkillRepository extends JpaRepository<KeySkill, Long> {

    Optional<KeySkill> findByName(String name);

    @Query("""
            SELECT new ru.comavp.vacancyscraper.entity.KeySkillCount(ks.name, COUNT(*))
            FROM Vacancy v JOIN v.keySkills ks
            WHERE v.experience = :experienceId
            GROUP BY ks.name
            ORDER BY COUNT(*) DESC
            """)
    List<KeySkillCount> getKeySkillMappingByExperienceId(@Param("experienceId") Long experienceId);

    @Query("""
            SELECT new ru.comavp.vacancyscraper.entity.KeySkillCount(ks.name, COUNT(*))
            FROM Vacancy v JOIN v.keySkills ks
            GROUP BY ks.name
            ORDER BY COUNT(*) DESC
            """)
    List<KeySkillCount> getKeySkillMapping();
}
