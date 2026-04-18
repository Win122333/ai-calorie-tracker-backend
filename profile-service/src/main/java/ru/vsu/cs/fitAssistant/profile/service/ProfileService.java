package ru.vsu.cs.fitAssistant.profile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vsu.cs.dto.AiResponse;
import ru.vsu.cs.dto.ProfileUpdateDto;
import ru.vsu.cs.fitAssistant.profile.entity.ProfileEntity;
import ru.vsu.cs.fitAssistant.profile.exception.ProfileNotFoundException;
import ru.vsu.cs.fitAssistant.profile.repository.ProfileRepository;
import ru.vsu.cs.fitAssistant.profile.repository.TargetRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final TargetRepository targetRepository;
    public List<ProfileEntity> getAll() {
        return profileRepository.findAll();
    }
    public Optional<ProfileEntity> getById(UUID id) {
        return profileRepository.findById(id);
    }
    @Transactional
    public ProfileEntity update(UUID id, ProfileUpdateDto dto) {
        ProfileEntity entityToUpdate = profileRepository.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException("Profile with id not found"));
        if (dto.height() != null)
            entityToUpdate.setHeight(dto.height());
        if (dto.activityLevel() != null)
            entityToUpdate.setActivityLevel(dto.activityLevel());
        if (dto.targetId() != null)
            entityToUpdate.setTarget(targetRepository.findById(dto.targetId())
                    .orElseThrow(() -> new NoSuchElementException("Target with id not found")));
        if (dto.weight() != null)
            entityToUpdate.setWeight(dto.weight());
        profileRepository.save(entityToUpdate);
        return entityToUpdate;
    }
    public AiResponse generateAiResponse(UUID id) {
        ProfileEntity profile = profileRepository.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException("Profile with id not found"));

    }
//    public ResponseProfileDto calculate(UUID id) {
//        ProfileEntity profile = getById(id).orElseThrow(() -> new NoSuchElementException("Profile with id not found"));
//        Double weight = profile.getWeight();
//        Integer height = profile.getHeight();
//        profile.get
//    }
}
