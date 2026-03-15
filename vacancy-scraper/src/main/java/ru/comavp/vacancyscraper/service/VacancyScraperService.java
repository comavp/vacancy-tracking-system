package ru.comavp.vacancyscraper.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.comavp.vacancyscraper.client.HHClient;

@Service
@RequiredArgsConstructor
public class VacancyScraperService {

    private final HHClient hhClient;

    @PostConstruct
    public void getVacancies() {
        String response = hhClient.getVacancies("java");
        System.out.println(response);
        getVacancyInfoById(1L);
    }

    public void getVacancyInfoById(Long id) {
        String response = hhClient.getVacancy(id);
        System.out.println("Vacancy info:");
        System.out.println(response);
    }
}
