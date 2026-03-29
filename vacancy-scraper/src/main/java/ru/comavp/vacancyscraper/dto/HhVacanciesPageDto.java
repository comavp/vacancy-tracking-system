package ru.comavp.vacancyscraper.dto;

import java.util.List;

public record HhVacanciesPageDto(int found, int pages, int page, int perPage, List<HhVacancyDto> items) {}
