package dev.peytob.mmo.server.core.service

import dev.peytob.mmo.server.core.resource.ExecutingWorldResource
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

private val log = LoggerFactory.getLogger(ExecutingWorldExecutionService::class.java)

@Service
class ExecutingWorldExecutionService {

    fun startExecutionWorldExecuting(executingWorld: ExecutingWorldResource) {
        log.info("Starting async executing cycle for executing world {}", executingWorld.id)
    }

    fun stopExecutionWorldExecuting(executingWorld: ExecutingWorldResource) {
        log.info("Terminating async executing cycle for executing world {}", executingWorld.id)
    }
}