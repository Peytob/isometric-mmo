package dev.peytob.mmo.server.core.resource

import dev.peytob.mmo.core.resource.Resource
import dev.peytob.mmo.core.resource.ResourceId
import dev.peytob.mmo.core.resource.annotation.DependsOnResource

class ExecutingWorldResource(
    override val id: ResourceId,

    @DependsOnResource
    val world: WorldResource
) : Resource
