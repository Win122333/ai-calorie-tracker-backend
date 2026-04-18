package ru.vsu.cs.fitAssistant.profile.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vsu.cs.dto.AiRequest;
import ru.vsu.cs.dto.AiResponse;
import ru.vsu.cs.dto.ProfileUpdateDto;
import ru.vsu.cs.fitAssistant.profile.client.AiRestClient;
import ru.vsu.cs.fitAssistant.profile.entity.ProfileEntity;
import ru.vsu.cs.fitAssistant.profile.exception.ProfileNotFoundException;
import ru.vsu.cs.fitAssistant.profile.mapper.AiMapper;
import ru.vsu.cs.fitAssistant.profile.mapper.ProfileMapper;
import ru.vsu.cs.fitAssistant.profile.repository.ProfileRepository;
import ru.vsu.cs.fitAssistant.profile.repository.TargetRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final TargetRepository targetRepository;
    private final AiRestClient aiRestClient;
    private final AiMapper aiMapper;
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
    public List<AiResponse> generateAiResponse(UUID id) {
        log.info("generate aiResponses");
        ProfileEntity profile = profileRepository.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException("Profile with id not found"));
        return aiRestClient.generateAiResponse(aiMapper.aiRequestFromProfile(profile));
    }
//    public ResponseProfileDto calculate(UUID id) {
//        ProfileEntity profile = getById(id).orElseThrow(() -> new NoSuchElementException("Profile with id not found"));
//        Double weight = profile.getWeight();
//        Integer height = profile.getHeight();
//        profile.get
//    }
}
