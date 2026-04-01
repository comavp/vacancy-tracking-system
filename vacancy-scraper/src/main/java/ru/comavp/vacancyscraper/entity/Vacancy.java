package ru.comavp.vacancyscraper.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vacancy")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vacancy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String originalId;

    @Column
    private String name;

    @Column
    private String url;

    @Column
    @Builder.Default
    private Boolean archived = false;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String originalData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id")
    private Employer employer;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "vacancy_key_skill",
            joinColumns = @JoinColumn(name = "vacancy_id"),
            inverseJoinColumns = @JoinColumn(name = "key_skill_id")
    )
    @Builder.Default
    private List<KeySkill> keySkills = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experience_id")
    private Experience experience;

    public void setEmployer(Employer employer) {
        this.employer = employer;
        employer.getVacancies().add(this);
    }

    public void addKeySkill(KeySkill keySkill) {
        keySkills.add(keySkill);
        keySkill.getVacancies().add(this);
    }
}
