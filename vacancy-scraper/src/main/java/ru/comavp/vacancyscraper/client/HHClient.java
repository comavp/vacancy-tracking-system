package ru.comavp.vacancyscraper.client;

import ru.comavp.vacancyscraper.dto.HhVacanciesPageDto;
import ru.comavp.vacancyscraper.dto.HhVacancyDto;

public interface HHClient {

    HhVacanciesPageDto getVacancies(Integer page, Integer perPage, String text,
                                    Integer professionalRole, String vacancySearchOrder);
    HhVacancyDto getVacancy(Long id);
}
