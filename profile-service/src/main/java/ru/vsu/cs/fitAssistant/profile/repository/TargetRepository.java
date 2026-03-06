package ru.vsu.cs.fitAssistant.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vsu.cs.fitAssistant.profile.entity.TargetEntity;

@Repository
public interface TargetRepository extends JpaRepository<TargetEntity, Integer> {
}
