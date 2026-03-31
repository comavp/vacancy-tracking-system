package ru.comavp.vacancyscraper.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import ru.comavp.vacancyscraper.dto.HhVacanciesPageDto;
import ru.comavp.vacancyscraper.dto.HhVacancyDto;

public interface HHClient {

    @GetExchange("/vacancies")
    HhVacanciesPageDto getVacancies(@RequestParam("page") Integer page,
                                    @RequestParam("per_page") Integer perPage,
                                    @RequestParam("text") String text,
                                    @RequestParam("professional_role") Integer professionalRole,
                                    @RequestParam("vacancy_search_order") String vacancySearchOrder);

    @GetExchange("/vacancies/{id}")
    HhVacancyDto getVacancy(@PathVariable("id") Long id);
}
