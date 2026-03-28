package ru.comavp.vacancyscraper.dto;

import java.util.List;

public record HhVacancyDto(
        String id,
        String name,
        String alternativeUrl,
        Boolean archived,
        HhEmployerDto employer,
        List<KeySkillDto> keySkills) {
}
