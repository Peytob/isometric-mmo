package dev.peytob.mmo.server.core.resource

import dev.peytob.mmo.core.resource.Resource
import dev.peytob.mmo.core.resource.annotation.DependsOnResource
import java.util.concurrent.Future
import java.util.concurrent.atomic.AtomicBoolean

class ExecutingWorldResource(
    override val id: String,

    @DependsOnResource
    val runningWorld: RunningWorldResource,

    val executingFuture: Future<Void>,

    var stopExecutingFlag: AtomicBoolean
) : Resource
