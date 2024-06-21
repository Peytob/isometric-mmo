package dev.peytob.mmo.server.network.http.mapper

import dev.peytob.mmo.server.network.model.ServerConnectionDetails
import dev.peytob.mmo.server.network.http.dto.ServerDetailsHttpResponse
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants
import java.net.URI

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
abstract class DetailsHttpMapper {

    @Mapping(target = "serverUrl", source = "selfUrl")
    abstract fun fromServiceDetailsToHttpDetails(serverConnectionDetails: ServerConnectionDetails): ServerDetailsHttpResponse

    fun uriToString(uri: URI): String = uri.toString()
}
