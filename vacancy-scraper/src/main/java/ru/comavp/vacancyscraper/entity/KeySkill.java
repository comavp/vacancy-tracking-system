package ru.comavp.vacancyscraper.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "key_skill")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeySkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String originalData;

    @ManyToMany(mappedBy = "keySkills")
    @Builder.Default
    private List<Vacancy> vacancies = new ArrayList<>();
}
