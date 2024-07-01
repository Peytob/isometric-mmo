package dev.peytob.mmo.core.resource.service

import dev.peytob.mmo.core.resource.ResourceId
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.stereotype.Service

@Service
class ResourceIdGenerator {

    fun generateResourceId(): ResourceId = RandomStringUtils.randomAlphanumeric(8)
}