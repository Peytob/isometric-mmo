package dev.peytob.mmo.backend.mapper

import dev.peytob.mmo.backend.controller.dto.CharacterResponse
import dev.peytob.mmo.backend.repository.entity.CharacterEntity
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants
import dev.peytob.mmo.backend.service.dto.Character

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
interface CharacterMapper {

    fun fromHibernateEntityToServiceDto(userEntity: CharacterEntity?): Character?

    fun fromServiceDtoToControllerDto(user: Character?): CharacterResponse?
}
