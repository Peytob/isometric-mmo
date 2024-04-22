package dev.peytob.mmo.backend.mapper

import dev.peytob.mmo.backend.repository.entity.UserEntity
import dev.peytob.mmo.backend.service.dto.User
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants.ComponentModel

@Mapper(
    componentModel = ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
interface UserMapper {

    fun fromHibernateEntityToServiceDto(userEntity: UserEntity): User
}
