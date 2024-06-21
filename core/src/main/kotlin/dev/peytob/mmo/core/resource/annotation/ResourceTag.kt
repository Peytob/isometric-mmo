package dev.peytob.mmo.core.resource.annotation

/**
 * Describes resource tag tip for managing resources.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class ResourceTag(
    val value: String
)
