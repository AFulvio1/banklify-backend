package com.afulvio.banklifybackend.mapper;

import com.afulvio.banklifybackend.model.entity.ClientEntity;
import com.afulvio.banklifybackend.model.request.RegisterRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    ClientEntity toClientEntity(RegisterRequest request);

}
