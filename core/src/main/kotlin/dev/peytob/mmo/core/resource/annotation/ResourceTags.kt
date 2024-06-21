package dev.peytob.mmo.core.resource.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class ResourceTags(
    val value: Array<ResourceTag>
)
