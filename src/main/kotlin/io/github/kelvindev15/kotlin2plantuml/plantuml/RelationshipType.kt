package io.github.kelvindev15.kotlin2plantuml.plantuml

/**
 * Types of supported UML relationships.
 */
enum class RelationshipType(
    /**
     * Default string representation of the relationship.
     */
    val right: String,
    /**
     * Reversed string representation of the relationship.
     */
    val left: String,
) {
    /**
     * A [EXTENDS] B will be translated to "A --|> B".
     */
    EXTENDS("--|>", "<|--"),
    /**
     * A [IMPLEMENTS] B will be translated to "A ..|> B".
     */
    IMPLEMENTS("..|>", "<|..");

    /**
     * String representation of this relationship.
     */
    override fun toString() = right
}
