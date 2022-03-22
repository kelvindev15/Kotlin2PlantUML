package io.github.kelvindev15.kotlin2plantuml.utils

import kotlin.reflect.*
import kotlin.reflect.full.createType
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.javaType
import kotlin.reflect.jvm.jvmErasure

class PlantUmlUtils {
    companion object {
        /**
         * Tries to load a [fullyQualifiedClass].
         * @return null if the class is not found.
         */
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
        fun  KVisibility?.plantUml() = when(this) {
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
            append(returnType.jvmErasure.simpleName)
        }

        /**
         * @return a plantuml representation of a [KParameter].
         */
        fun KParameter.plantUml() = "${name}: ${type.jvmErasure.simpleName}"

        /**
         * @return a plantuml string for representing a [KClass] type parameters.
         */
        fun List<KTypeParameter>.plantUml(): String =
            if (isNotEmpty())
                joinToString(prefix = "<", separator = ", ", postfix = ">") { it.plantUml() }
            else ""

        /**
         * @return a plantuml representation o [KTypeParameter].
         */
        fun KTypeParameter.plantUml(): String = buildString {
            this@plantUml.upperBounds.minus(Any::class.createType(nullable = true)).let {
                if (it.isNotEmpty()) {
                    append("${this@plantUml.name} : ")
                    it.forEach { t ->
                        append(t.jvmErasure.simpleName)
                        val args = t.arguments.mapNotNull { a -> a.type }
                        if (args.isNotEmpty()) {
                            append(
                                args.joinToString(prefix = "<", postfix = ">") { b ->
                                    loadClassOrNull(b.javaType.typeName)
                                        ?.plantUml()
                                        ?: b.javaType.typeName
                                }
                            )
                        }
                    }
                } else append(this@plantUml.name)
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
            if(isInterface) {
                append("interface ")
            } else {
                append("class ")
            }
            append(simpleName)
            append(typeParameters.plantUml())
        }
    }
}
