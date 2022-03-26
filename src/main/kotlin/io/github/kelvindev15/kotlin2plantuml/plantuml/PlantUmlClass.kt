package io.github.kelvindev15.kotlin2plantuml.plantuml

import io.github.kelvindev15.kotlin2plantuml.utils.ReflectUtils.Companion.canShow
import io.github.kelvindev15.kotlin2plantuml.utils.ReflectUtils.Companion.loadClassOrThrow
import io.github.kelvindev15.kotlin2plantuml.utils.ReflectUtils.Companion.plantUml
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMembers

/**
 * A plantUML class.
 */
class PlantUmlClass(
    private val classRef: KClass<*>,
    private val configuration: Configuration,
) {
    constructor(
        fullyQualifiedClass: String,
        configuration: Configuration = Configuration(),
    ) : this(loadClassOrThrow(fullyQualifiedClass), configuration)

    constructor(
        simpleClassName: String,
        packages: List<String> = listOf("io.github.kelvindev15.plantUmlGenerator.plantuml"),
        configuration: Configuration = Configuration(),
    ) : this(loadClassOrThrow(packages, simpleClassName), configuration)

    /**
     * Returns [classRef]'s PlantUML string.
     */
    fun plantUml(): String = buildString {
        append(classRef.plantUml())
        append(" {")
        appendLine()
        classRef.declaredMembers.filter {
            it.visibility?.let { visibility ->
                when (it) {
                    is KFunction<*> ->
                        !configuration.hideMethods && visibility.canShow(configuration.maxMethodVisibility)
                    is KProperty<*> ->
                        !configuration.hideFields && visibility.canShow(configuration.maxFieldVisibility)
                    else -> false
                }
            } ?: false
        }.forEach {
            append("    ${it.plantUml()}")
            appendLine()
        }
        append("}\n")
    }
}
