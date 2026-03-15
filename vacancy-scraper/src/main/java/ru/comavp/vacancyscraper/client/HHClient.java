package ru.comavp.vacancyscraper.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface HHClient {

    @GetExchange("/vacancies")
    String getVacancies(@RequestParam("text") String text);

    @GetExchange("/vacancies/{id}")
    String getVacancy(@PathVariable("id") Long id);
}
