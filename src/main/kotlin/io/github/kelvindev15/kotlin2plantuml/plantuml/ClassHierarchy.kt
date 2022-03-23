package io.github.kelvindev15.kotlin2plantuml.plantuml

import io.github.classgraph.ClassGraph
import io.github.kelvindev15.kotlin2plantuml.utils.PlantUmlUtils.Companion.canShow
import io.github.kelvindev15.kotlin2plantuml.utils.PlantUmlUtils.Companion.isInterface
import org.jgrapht.Graph
import org.jgrapht.graph.DefaultDirectedGraph
import kotlin.reflect.KClass
import kotlin.reflect.full.superclasses

class ClassHierarchy(
    val rootClass: KClass<*>,
    private val configuration: Configuration = Configuration(),
) : Graph<KClass<*>, PlantUmlRelationship> by DefaultDirectedGraph(PlantUmlRelationship::class.java) {

    init {
        addVertex(rootClass)
        // GRAPH VERTICES
        ClassGraph().acceptPackages(rootClass.java.packageName).scan().let {
            if (rootClass.isInterface) {
                it.getClassesImplementing(rootClass.java)
            } else {
                it.getSubclasses(rootClass.java)
            }
        }.loadClasses().forEach { addVertex(it.kotlin) }
        // GRAPH EDGES
        vertexSet().forEach {
            it.superclasses.filter { c -> c != Any::class && c in vertexSet() }.forEach { superclass ->
                addEdge(
                    it,
                    superclass,
                    PlantUmlRelationship(
                        if(it.isInterface != superclass.isInterface) {
                            RelationshipType.IMPLEMENTS
                        } else {
                            RelationshipType.IMPLEMENTS
                        }
                    )
                )
            }
        }
    }

    private fun entities() =  buildString {
        vertexSet()
            .forEach {
                append(PlantUmlClass(it, configuration).plantUml())
                appendLine()
            }
    }
    private fun relationships() = buildString {
        edgeSet().forEach {
            append(it.plantUml())
            appendLine()
        }
    }

    private fun plantUmlHierarchy() = buildString {
        append(entities())
        if (!configuration.hideRelationships) {
            append(relationships())
        }
    }

    /**
     * PlantUml representation of [rootClass].
     * if [configuration]'s recurse is set to true the entire [rootClass] hierarchy
     * it's explored, otherwise only [rootClass] will be the plantuml output.
     */
    fun plantUml(): String =
        if (configuration.recurse) {
            plantUmlHierarchy()
        }
        else {
            PlantUmlClass(rootClass, configuration).plantUml()
        }
}