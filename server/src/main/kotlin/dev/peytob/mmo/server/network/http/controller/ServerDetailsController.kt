package dev.peytob.mmo.server.network.http.controller

import dev.peytob.mmo.server.core.service.ServerDetailsService
import dev.peytob.mmo.server.network.http.dto.ServerDetailsHttpResponse
import dev.peytob.mmo.server.network.http.mapper.DetailsHttpMapper
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/details")
class ServerDetailsController(
    private val serverDetailsService: ServerDetailsService,
    private val serverDetailsHttpMapper: DetailsHttpMapper
) {

    @GetMapping
    fun getServerConnectionDetails(): ServerDetailsHttpResponse {
        val serverConnectionDetails = serverDetailsService.getServerConnectionDetails()
        return serverDetailsHttpMapper.fromServiceDetailsToHttpDetails(serverConnectionDetails)
    }
}
