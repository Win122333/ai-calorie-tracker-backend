package ru.vsu.cs.fitAssistant.profile.mapper;

import ru.vsu.cs.dto.ProfileUpdateDto;
import ru.vsu.cs.dto.RequestProfileDto;
import ru.vsu.cs.dto.ResponseProfileDto;
import ru.vsu.cs.fitAssistant.profile.entity.ProfileEntity;

@org.mapstruct.Mapper(componentModel = "spring")
public interface Mapper {
    RequestProfileDto toRequest(ProfileEntity entity);
    ResponseProfileDto toResponse(ProfileEntity entity);
}
