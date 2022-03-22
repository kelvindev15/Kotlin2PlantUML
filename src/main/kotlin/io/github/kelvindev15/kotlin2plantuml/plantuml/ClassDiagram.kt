package io.github.kelvindev15.kotlin2plantuml.plantuml

import kotlin.reflect.KClass

class ClassDiagram(
    private vararg val roots: ClassHierarchy,
) {
    constructor(
        vararg classRefs: KClass<*>,
        configuration: Configuration = Configuration(),
    ) : this(*classRefs.map { ClassHierarchy(it, configuration) }.toTypedArray())

    /**
     * @return a plantuml representation of this [ClassDiagram].
     */
    fun plantUml() = buildString {
        append("@startuml")
        appendLine()
        roots.distinct().forEach {
            append(it.plantUml())
        }
        append("@enduml")
    }
}
