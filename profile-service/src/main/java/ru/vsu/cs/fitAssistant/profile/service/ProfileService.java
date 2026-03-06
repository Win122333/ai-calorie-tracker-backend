package ru.vsu.cs.fitAssistant.profile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vsu.cs.fitAssistant.profile.entity.ProfileEntity;
import ru.vsu.cs.fitAssistant.profile.repository.ProfileRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    public List<ProfileEntity> getAll() {
        return profileRepository.findAll();
    }
    public Optional<ProfileEntity> getById(Long id) {
        return profileRepository.findById(id);
    }
}
