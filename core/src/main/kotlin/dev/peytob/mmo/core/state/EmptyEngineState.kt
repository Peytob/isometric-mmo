package dev.peytob.mmo.core.state

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.stereotype.Component

private val log = LoggerFactory.getLogger(EmptyEngineState::class.java)

@ConditionalOnMissingBean(name = [ENTRYPOINT_STATE])
@Qualifier(ENTRYPOINT_STATE)
@Component
class EmptyEngineState : EngineState {

    override fun isTerminated(): Boolean {
        log.error("No entrypoint state found. Terminating.")
        return true
    }

    override fun tick() {
        // Nothing
    }
}