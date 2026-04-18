package ru.vsu.cs.fitAssistant.profile.mapper;

import org.mapstruct.Mapper;
import ru.vsu.cs.dto.AiRequest;
import ru.vsu.cs.fitAssistant.profile.entity.ProfileEntity;

@Mapper(componentModel = "spring")
public interface AiMapper {
    AiRequest aiRequestFromProfile(ProfileEntity entity);
}
