package ru.comavp.vacancyscraper.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
@Getter
@Setter
public class VacancyScraperProperties {

    private String text = "java";
    private Integer page = 0;
    private Integer perPage = 20;
    private Integer professionalRole = 96; // Программист, разработчик
    private String vacancySearchOder = "publication_time";
}
