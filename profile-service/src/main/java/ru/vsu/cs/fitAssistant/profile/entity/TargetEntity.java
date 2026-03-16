package ru.vsu.cs.fitAssistant.profile.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_target")
public class TargetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "c_alias")
    @Enumerated(EnumType.STRING)
    private ActivityTarget activityTarget;
}
