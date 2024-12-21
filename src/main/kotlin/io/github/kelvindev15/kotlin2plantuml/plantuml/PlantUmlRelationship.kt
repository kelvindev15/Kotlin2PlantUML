package io.github.kelvindev15.kotlin2plantuml.plantuml

import org.jgrapht.graph.DefaultEdge
import kotlin.reflect.KClass

/**
 * A plantUML relationship.
 */
class PlantUmlRelationship(
    private val relationshipType: RelationshipType,
) : DefaultEdge() {
    /**
     * Source class of the relationship.
     */
    public override fun getSource(): KClass<*> = super.getSource() as KClass<*>

    /**
     * Destination class of the relationship.
     */
    public override fun getTarget(): KClass<*> = super.getTarget() as KClass<*>

    /**
     * Plant representation of an uml relationship.
     */
    fun plantUml(): String = "${target.simpleName} ${relationshipType.left} ${source.simpleName}"
}
