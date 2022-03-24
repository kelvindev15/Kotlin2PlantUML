package io.github.kelvindev15.kotlin2plantuml

import io.github.kelvindev15.kotlin2plantuml.plantuml.ClassDiagram
import io.github.kelvindev15.kotlin2plantuml.plantuml.Configuration
import io.github.kelvindev15.kotlin2plantuml.utils.ReflectUtils
import java.io.File
import kotlin.reflect.KVisibility

/**
 * Returns the cli argument in [args] relative to any of passed [options].
 * If the none is found, null is returned.
 */
fun getOption(args: Array<String>, vararg options: String): String? = options
    .find { it in args }
    ?.let {
        args.elementAt(args.indexOf(it) + 1)
    }

/**
 * Gets the cli visibility option for [args].
 */
fun getVisibilityOption(args: Array<String>, vararg options: String): KVisibility =
    getOption(args, *options)?.let { KVisibility.values()[it.toInt()] } ?: KVisibility.PUBLIC

/**
 * Returns an instance of a run configuration based on cli [args].
 */
fun toConfiguration(args: Array<String>): Configuration = Configuration(
    hideFields = listOf("-hf", "--hide-fields").any { it in args },
    hideMethods = listOf("-hm", "--hide-methods").any { it in args },
    hideRelationships = listOf("-hr", "--hide-relationships").any { it in args },
    recurse = listOf("-r", "--recurse").any { it in args },
    maxFieldVisibility = getVisibilityOption(args, "-fv", "--field-visibility"),
    maxMethodVisibility = getVisibilityOption(args, "-mv", "--method-visibility"),
)

/**
 * ---------------------------------------------------
 * [args]: cli arguments
 * ---------------------------------------------------
 * fullyQualifiedClass [...options]
 *
 * ---------------------------------------------------
 * Options:
 * ---------------------------------------------------
 * -o, --output: output file
 * -r, --recurse: explore class hierarchy recursively
 *
 * -hf, --hide-fields: hide fields
 * -hm, --hide-methods: hide methods
 * -hr, --hide-relationships: hide relationships
 *
 * Visibility:
 * Show elements with up to <visibility> modifier.
 * The order is defined by the KVisibility enum:
 * { 0: Public, 1: Protected, 2: Internal, 3: Private }
 *
 * -fv, --field-visibility: max field visibility
 * -mv, --method-visibility: max method visibility
 */
fun main(args: Array<String>) {
    require(args.isNotEmpty()) {
        "No fully qualified input class had been provided"
    }
    val outputFile = getOption(args, "-o")
        ?: "build${File.separatorChar}reports${File.separatorChar}diagram.plantuml"
    val clazz = ReflectUtils.loadClassOrThrow(args[0])
    File(outputFile).apply {
        parentFile.mkdirs()
        createNewFile()
        writeText(ClassDiagram(clazz, configuration = toConfiguration(args)).plantUml())
    }
}
