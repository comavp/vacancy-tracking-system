package ru.comavp.vacancyscraper.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "experience")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String originalId;

    @Column
    private String name;

    @OneToMany(mappedBy = "experience", fetch = FetchType.LAZY)
    private List<Vacancy> vacancies;
}
