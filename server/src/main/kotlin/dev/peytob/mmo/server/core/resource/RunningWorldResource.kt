package dev.peytob.mmo.server.core.resource

import dev.peytob.ecs.context.EcsContext
import dev.peytob.mmo.core.resource.Resource
import dev.peytob.mmo.core.resource.ResourceId
import dev.peytob.mmo.core.resource.annotation.DependsOnResource

class RunningWorldResource(
    override val id: ResourceId,

    @DependsOnResource
    val world: WorldResource,

    val ecsContext: EcsContext
) : Resource
