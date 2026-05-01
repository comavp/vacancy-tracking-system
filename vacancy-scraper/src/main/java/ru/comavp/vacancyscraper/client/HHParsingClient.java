package ru.comavp.vacancyscraper.client;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.comavp.vacancyscraper.dto.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Primary
public class HHParsingClient implements HHClient {

    private static final String HH_SEARCH_URL = "https://samara.hh.ru/search/vacancy?text=java";
    public static final String HH_GET_VACANCY_URL = "https://samara.hh.ru/vacancy/%d";
    private static final int HH_TIMEOUT = 20_000;

    @Override
    public HhVacanciesPageDto getVacancies(Integer page, Integer perPage, String text,
                                           Integer professionalRole, String vacancySearchOrder) {
        try {
            Document doc = Jsoup.connect(HH_SEARCH_URL)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(HH_TIMEOUT)
                    .get();
            int vacancyCount = parseVacancyCount(doc);
            int pageCount = parsePageCount(doc);
            List<HhVacancyDto> vacancyDtoList = parseVacancies(doc);
            return new HhVacanciesPageDto(vacancyCount, pageCount, 0, 0, vacancyDtoList);
        } catch (IOException e) {
            throw new RuntimeException("Unexpected exception during retrieving html from HH", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception during parsing data from HH", e);
        }
    }

    private int parseVacancyCount(Document doc) {
        // Ищем span, текст которого содержит "вакансий"
        for (Element span : doc.select("span")) {
            String text = span.text().trim();
            if (text.contains("вакансий") || text.contains("вакансия") || text.contains("вакансии")) {
                String digits = text.replaceAll("[^0-9]", "");
                if (!digits.isEmpty()) {
                    return Integer.parseInt(digits);
                }
            }
        }
        return 0;
    }

    public static int parsePageCount(Document doc) {
        // Собираем все pager-page элементы (кроме кнопки "вперёд")
        Elements pageLinks = doc.select("a[data-qa=pager-page]");
        int maxPage = 0;
        for (Element link : pageLinks) {
            String text = link.text().trim();
            try {
                int page = Integer.parseInt(text);
                if (page > maxPage) maxPage = page;
            } catch (NumberFormatException ignored) {}
        }
        // Если пагинации нет вовсе — значит страница одна
        return maxPage > 0 ? maxPage : 1;
    }

    public static List<HhVacancyDto> parseVacancies(Document doc) {
        List<HhVacancyDto> result = new ArrayList<>();

        // Ссылка на вакансию: тег <a> с data-qa="serp-item__title"
        Elements links = doc.select("a[data-qa=serp-item__title]");

        for (Element link : links) {
            String href = link.attr("href"); // alternateUrl
            String id = extractId(href); // id из URL

            // Название вакансии: span с data-qa="serp-item__title-text"
            Element nameSpan = link.selectFirst("span[data-qa=serp-item__title-text]");
            // У span могут быть вложенные теги (svg и т.п.) — берём только ownText
            String name = nameSpan != null ? nameSpan.ownText().trim() : "";

            // Поднимаемся к общему контейнеру карточки вакансии
            Element vacancyCard = link.closest("[data-qa=\"vacancy-serp__vacancy\"]");
            String employerId = "";
            String employerName = "";
            String employerUrl = "";

            if (vacancyCard != null) {
                // Ищем ссылку работодателя внутри этой карточки
                Element employerLink = vacancyCard.selectFirst("a[data-qa=\"vacancy-serp__vacancy-employer\"]");
                if (employerLink != null) {
                    employerUrl = employerLink.attr("href"); // относительный URL
                    employerId = extractIdFromEmployerUrl(employerUrl);

                    Element employerTextSpan = employerLink.selectFirst(
                            "span[data-qa=\"vacancy-serp__vacancy-employer-text\"]");
                    employerName = employerTextSpan != null ?
                            employerTextSpan.text().trim() : "";
                }
            }

            // Опыт
            HHExperienceDto experience = parseExperience(vacancyCard);

            if (!id.isEmpty() && !name.isEmpty()) {
                result.add(new HhVacancyDto(
                        id,
                        name,
                        href,
                        false,
                        new HhEmployerDto(employerId, employerName, employerUrl),
                        experience,
                        null)
                );
            }
        }
        return result;
    }

    private static HHExperienceDto parseExperience(Element vacancyCard) {
        if (vacancyCard == null) return null;

        // Ищем span с data-qa, начинающимся на "vacancy-serp__vacancy-work-experience-"
        Element expSpan = vacancyCard.selectFirst("span[data-qa^=\"vacancy-serp__vacancy-work-experience-\"]");
        if (expSpan == null) return null;

        String qa = expSpan.attr("data-qa");
        String id = qa.substring(qa.lastIndexOf('-') + 1); // получаем "noExperience", "between1And3" и т.д.
        String name = expSpan.text().trim(); // "Нет опыта", "От 1 года до 3 лет" и т.д.

        return new HHExperienceDto(id, name);
    }

    private static String extractId(String url) {
        Pattern pattern = Pattern.compile("/vacancy/(\\d+)");
        Matcher matcher = pattern.matcher(url);
        return matcher.find() ? matcher.group(1) : "";
    }

    private static String extractIdFromEmployerUrl(String url) {
        Pattern pattern = Pattern.compile("/employer/(\\d+)");
        Matcher matcher = pattern.matcher(url);
        return matcher.find() ? matcher.group(1) : "";
    }

    @Override
    public HhVacancyDto getVacancy(Long id) {
        try {
            String url = HH_GET_VACANCY_URL.formatted(id);
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(HH_TIMEOUT)
                    .get();
            List<KeySkillDto> keySkills = parseSkills(doc);
            return new HhVacancyDto(String.valueOf(id), "", url, false, null, null, keySkills);
        } catch (IOException e) {
            throw new RuntimeException("Unexpected exception during retrieving html from HH", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception during parsing data from HH", e);
        }
    }

    public static List<KeySkillDto> parseSkills(Document doc) {
        List<String> skills = new ArrayList<>();
        // Ищем все li с data-qa="skills-element", внутри div с классом метки навыка
        Elements skillElements = doc.select("li[data-qa=skills-element] div.magritte-tag__label___YHV-o_5-2-2");
        for (Element skillDiv : skillElements) {
            String skill = skillDiv.text().trim();
            // Заменяем неразрывные пробелы (&nbsp;) на обычные
            skill = skill.replace('\u00A0', ' ');
            if (!skill.isEmpty()) {
                skills.add(skill);
            }
        }
        return skills.stream().map(KeySkillDto::new).toList();
    }
}
