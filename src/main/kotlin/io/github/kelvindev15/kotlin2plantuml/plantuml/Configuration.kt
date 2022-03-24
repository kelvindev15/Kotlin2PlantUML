package io.github.kelvindev15.kotlin2plantuml.plantuml

import kotlin.reflect.KVisibility

/**
 * The plantuml display configuration.
 */
data class Configuration(
    /**
     * If set to true, [PlantUmlClass] using this [Configuration]
     * will hide all their fields.
     */
    val hideFields: Boolean = false,
    /**
     * If set to true, [PlantUmlClass] using this [Configuration]
     * will hide all their member functions.
     */
    val hideMethods: Boolean = false,
    /**
     * If set to true, [PlantUmlClass] using this [Configuration]
     * will not declare their relationships.
     */
    val hideRelationships: Boolean = false,
    /**
     * If set to true, [PlantUmlClass] using this [Configuration]
     * will visit recursively all their subclasses.
     */
    val recurse: Boolean = true,
    /**
     * [PlantUmlClass] using this [Configuration]
     * will hide fields with a visibility level grater than
     * [maxFieldVisibility].
     *
     * note: the visibility level order is defined by [KVisibility].
     */
    val maxFieldVisibility: KVisibility = KVisibility.PUBLIC,
    /**
     * [PlantUmlClass] using this [Configuration]
     * will hide member functions with a visibility level grater than
     * [maxMethodVisibility].
     *
     * note: the visibility level order is defined by [KVisibility].
     */
    val maxMethodVisibility: KVisibility = KVisibility.PUBLIC,
)
