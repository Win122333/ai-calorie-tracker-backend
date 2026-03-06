package ru.vsu.cs.fitAssistant.profile.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Setter
@Getter
@Table(name = "t_user_profile")
@NoArgsConstructor
@AllArgsConstructor
public class ProfileEntity {
    @Id
    private UUID id;
    @Column(name = "c_gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(name = "c_name")
    private String name;
    @Column(name = "c_surname")
    private String surname;
    @Column(name = "c_birth_date")
    private LocalDate birthday;
    @Column(name = "c_weight")
    private Double weight;
    @Column(name = "c_height")
    private Integer height;
    @Column(name = "c_activity_level")
    private Double activityLevel;
    @Column(name = "c_weekly_budget")
    private Double weeklyBudget;

    @ManyToOne
    @JoinColumn(name = "id")
    private TargetEntity target;
}
