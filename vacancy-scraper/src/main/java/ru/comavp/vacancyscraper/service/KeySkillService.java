package ru.comavp.vacancyscraper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.comavp.vacancyscraper.entity.KeySkillCount;
import ru.comavp.vacancyscraper.repository.ExperienceRepository;
import ru.comavp.vacancyscraper.repository.KeySkillRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KeySkillService {

    private final KeySkillRepository keySkillRepository;
    private final ExperienceRepository experienceRepository;

    public List<KeySkillCount> findTopKeySkillsByExperience(String experience, String limit) {
        if (experience != null) {
            Long experienceId = experienceRepository.findByOriginalId(experience)
                    .orElseThrow(() -> new RuntimeException("В справочнике не найдена категория с originalId = '" + experience + "'"))
                    .getId();
            // todo исправить exception
            /*
            * org.springframework.dao.InvalidDataAccessApiUsageException:
            * Argument [3] of type [java.lang.Long] did not match parameter type [ru.comavp.vacancyscraper.entity.Experience
            * */
            return keySkillRepository.getKeySkillMappingByExperienceId(experienceId);
        }
        return keySkillRepository.getKeySkillMapping();
    }
}
