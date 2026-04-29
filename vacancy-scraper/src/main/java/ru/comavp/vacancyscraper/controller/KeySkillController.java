package ru.comavp.vacancyscraper.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.comavp.vacancyscraper.entity.KeySkillCount;
import ru.comavp.vacancyscraper.service.KeySkillService;

import java.util.List;

@RestController
@RequestMapping("api/key-skills")
@RequiredArgsConstructor
public class KeySkillController {

    private final KeySkillService keySkillService;

    @GetMapping
    public ResponseEntity<List<KeySkillCount>> findTopKeySkillsByExperience(@RequestParam(value = "experience", required = false) String experience,
                                                                            @RequestParam(value = "limit", required = false) String limit) {
        return ResponseEntity.ok(keySkillService.findTopKeySkillsByExperience(experience, limit));
    }
}
