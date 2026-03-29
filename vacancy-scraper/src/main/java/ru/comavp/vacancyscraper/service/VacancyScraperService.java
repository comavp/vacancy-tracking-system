package ru.comavp.vacancyscraper.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.comavp.vacancyscraper.client.HHClient;
import ru.comavp.vacancyscraper.dto.HhEmployerDto;
import ru.comavp.vacancyscraper.dto.HhVacancyDto;
import ru.comavp.vacancyscraper.dto.KeySkillDto;
import ru.comavp.vacancyscraper.entity.Employer;
import ru.comavp.vacancyscraper.entity.KeySkill;
import ru.comavp.vacancyscraper.entity.Vacancy;
import ru.comavp.vacancyscraper.repository.EmployerRepository;
import ru.comavp.vacancyscraper.repository.KeySkillRepository;
import ru.comavp.vacancyscraper.repository.VacancyRepository;

@Service
@RequiredArgsConstructor
public class VacancyScraperService {

    @Value("${search-query:java}")
    private String searchQuery;

    private final HHClient hhClient;
    private final EmployerRepository employerRepository;
    private final VacancyRepository vacancyRepository;
    private final KeySkillRepository keySkillRepository;
    private final TransactionTemplate transactionTemplate;

    @PostConstruct // todo
    public void getVacancies() {
        var response = hhClient.getVacancies(searchQuery);
        transactionTemplate.execute(status -> {
            response.items().forEach(hhVacancyDto -> {
                var employerDto = hhVacancyDto.employer();
                var existingEmployer = employerRepository.findByOriginalId(employerDto.id())
                        .orElseGet(() -> employerRepository.save(mapToEmployer(hhVacancyDto.employer())));
                var extendedVacancyDto = getVacancyInfoById(Long.valueOf(hhVacancyDto.id()));
                var keySkillsList = extendedVacancyDto.keySkills()
                        .stream()
                        .map(keySkillDto -> keySkillRepository.findByName(keySkillDto.name())
                                .orElseGet(() -> keySkillRepository.save(mapToKeySkill(keySkillDto))))
                        .toList();
                var existingVacancy = vacancyRepository.findByOriginalId(hhVacancyDto.id())
                        .orElseGet(() -> {
                            var vacancy = mapToVacancy(hhVacancyDto);
                            vacancy.setEmployer(existingEmployer);
                            keySkillsList.forEach(vacancy::addKeySkill);
                            return vacancyRepository.save(vacancy);
                        });
            });
            return null;
        });
    }

    public HhVacancyDto getVacancyInfoById(Long id) {
        return hhClient.getVacancy(id);
    }

    private Employer mapToEmployer(HhEmployerDto hhEmployerDto) {
        return Employer.builder()
                .originalId(hhEmployerDto.id())
                .name(hhEmployerDto.name())
                .url(hhEmployerDto.alternateUrl())
                .build();
    }

    private Vacancy mapToVacancy(HhVacancyDto hhVacancyDto) {
        return Vacancy.builder()
                .name(hhVacancyDto.name())
                .originalId(hhVacancyDto.id())
                .archived(hhVacancyDto.archived())
                .url(hhVacancyDto.alternateUrl())
                .build();
    }

    private KeySkill mapToKeySkill(KeySkillDto keySkillDto) {
        return KeySkill.builder()
                .name(keySkillDto.name())
                .build();
    }
}
