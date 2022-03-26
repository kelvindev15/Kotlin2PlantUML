package io.github.kelvindev15.kotlin2plantuml.utils

import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.KTypeParameter
import kotlin.reflect.KVisibility
import kotlin.reflect.full.createType
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.jvmErasure

/**
 * PlantUML utilities.
 */
class ReflectUtils private constructor() {
    companion object {
        private fun loadClassOrNull(fullyQualifiedClass: String): KClass<*>? {
            var result: KClass<*>? = null
            try {
                result = ClassLoader.getSystemClassLoader().loadClass(fullyQualifiedClass).kotlin
            } catch (_: ClassNotFoundException) { }
            return result
        }

        /**
         * Tries to load [fullyQualifiedClass].
         * @return null if [fullyQualifiedClass] is not found in the classpath.
         */
        fun loadClassOrThrow(
            fullyQualifiedClass: String,
            lazyMessage: () -> String = { "Unable to find $fullyQualifiedClass" }
        ): KClass<*> = loadClassOrNull(fullyQualifiedClass) ?: throw ClassNotFoundException(lazyMessage())

        /**
         * Tries to load [simpleClassName] by searching in [packages].
         * @return null if [simpleClassName] is not found in any of the provided [packages].
         */
        fun loadClassOrThrow(
            packages: Iterable<String>,
            simpleClassName: String,
            lazyMessage: () -> String = { "Unable to find $simpleClassName in the provided packages" }
        ): KClass<*> = packages.mapNotNull { loadClassOrNull("$it.$simpleClassName") }
            .also { require(it.isNotEmpty(), lazyMessage) }
            .first()

        /**
         * @return a character corresponding to the plantuml visibility specification.
         */
        fun KVisibility?.plantUml() = when (this) {
            KVisibility.PUBLIC -> "+"
            KVisibility.PROTECTED -> "#"
            KVisibility.INTERNAL -> "~"
            KVisibility.PRIVATE -> "-"
            else -> ""
        }

        /**
         * @return true if entity with this [KVisibility] can be displayed.
         */
        fun KVisibility?.canShow(maxVisibility: KVisibility): Boolean = this != null && this <= maxVisibility

        /**
         * @return a plantuml representation of a [KCallable] (field or member function).
         */
        fun KCallable<*>.plantUml() = buildString {
            append(visibility.plantUml())
            if (isAbstract) {
                append("{abstract} ")
            }
            append(name)
            if (this@plantUml is KFunction) {
                append(valueParameters.joinToString(prefix = "(", separator = ", ", postfix = ")") { it.plantUml() })
            }
            append(": ")
            append(returnType.plantUml())
        }

        /**
         * @return a plantuml representation of a [KParameter].
         */
        fun KParameter.plantUml() = "$name: ${type.jvmErasure.simpleName}"

        /**
         * @return a plantuml representation o [KTypeParameter].
         */
        fun KTypeParameter.plantUml(): String = buildString {
            val upperBounds = this@plantUml.upperBounds.minus(Any::class.createType(nullable = true))
            if (upperBounds.isNotEmpty()) {
                append(upperBounds.joinToString(separator = ",\\n") { "${this@plantUml.name} : ${it.plantUml()}" })
            } else append(this@plantUml.name)
        }

        /**
         * PlantUml representation of [KType].
         */
        fun KType.plantUml() = buildString {
            append(jvmErasure.simpleName)
            if (arguments.isNotEmpty()) {
                append(arguments.joinToString(prefix = "<", postfix = ">") { it.toString().substringAfterLast(".") })
            }
        }

        /**
         * @return true if this [KClass] is an interface
         */
        val KClass<*>.isInterface get() = java.isInterface

        /**
         * @return a plantuml representation of a [KClass].
         */
        fun KClass<*>.plantUml(): String = buildString {
            if (isAbstract && !isInterface) {
                append("abstract ")
            }
            if (isInterface) {
                append("interface ")
            } else {
                append("class ")
            }
            append(simpleName)
            if (typeParameters.isNotEmpty()) {
                append(typeParameters.joinToString(prefix = "<", separator = ",\\n", postfix = ">") { it.plantUml() })
            }
        }
    }
}
