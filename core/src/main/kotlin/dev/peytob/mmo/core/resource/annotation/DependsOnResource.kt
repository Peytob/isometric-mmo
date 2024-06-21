package dev.peytob.mmo.core.resource.annotation

/**
 * Describes the relationship to the resources on which the resource depends.
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class DependsOnResource
