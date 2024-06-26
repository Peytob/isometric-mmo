package dev.peytob.mmo.server.core.resource

import dev.peytob.mmo.core.resource.Resource
import dev.peytob.mmo.core.resource.ResourceId
import dev.peytob.mmo.core.resource.annotation.DependsOnResource

class ExecutingWorldResource(
    override val id: ResourceId,

    @DependsOnResource
    val world: WorldResource,

    val state: ExecutingWorldState,

//    val ecsContext: EcsContext
) : Resource {

    enum class ExecutingWorldState(
        val isTerminated: Boolean
    ) {
        PENDING(
            isTerminated = false
        ),
        EXECUTING(
            isTerminated = false
        ),
        EXIT_SUCCESS(
            isTerminated = true
        ),
        EXIT_FAIL(
            isTerminated = true
        )
    }
}
