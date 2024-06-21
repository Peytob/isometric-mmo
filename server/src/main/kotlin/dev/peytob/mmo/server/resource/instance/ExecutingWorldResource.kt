package dev.peytob.mmo.server.resource.instance

import dev.peytob.mmo.core.resource.Resource
import dev.peytob.mmo.core.resource.ResourceId
import dev.peytob.mmo.core.resource.annotation.DependsOnResource

class ExecutingWorldResource(
    override val id: ResourceId,

    @DependsOnResource
    val world: WorldResource
) : Resource
