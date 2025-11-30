package com.afulvio.banklifybackend.mapper;

import com.afulvio.banklifybackend.model.dto.UserProfileDTO;
import com.afulvio.banklifybackend.model.entity.ClientEntity;
import com.afulvio.banklifybackend.model.request.RegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(target = "registrationDate", expression = "java(java.time.LocalDateTime.now())")
    ClientEntity toClientEntity(RegisterRequest request, String passwordHash);

    UserProfileDTO toUserProfile(ClientEntity clientEntity);

}
