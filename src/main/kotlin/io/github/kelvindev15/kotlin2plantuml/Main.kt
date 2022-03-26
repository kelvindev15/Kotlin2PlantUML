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
 * Gets the cli field visibility option from [args].
 */
fun getFieldVisibility(args: Array<String>): KVisibility =
    getOption(args, "-fv", "--field-visibility")?.let { KVisibility.values()[it.toInt()] } ?: KVisibility.PUBLIC

/**
 * Gets the cli method visibility option from [args].
 */
fun getMethodVisibility(args: Array<String>): KVisibility =
    getOption(args, "-fm", "--method-visibility")?.let { KVisibility.values()[it.toInt()] } ?: KVisibility.PUBLIC

/**
 * @return a list of packages passed al cli [args].
 */
fun getPackages(args: Array<String>): List<String> =
    getOption(args, "-p", "--packages")?.split(":") ?: emptyList()

/**
 * Returns an instance of a run configuration based on cli [args].
 */
fun toConfiguration(args: Array<String>): Configuration = Configuration(
    hideFields = listOf("-hf", "--hide-fields").any { it in args },
    hideMethods = listOf("-hm", "--hide-methods").any { it in args },
    hideRelationships = listOf("-hr", "--hide-relationships").any { it in args },
    recurse = listOf("-r", "--recurse").any { it in args },
    maxFieldVisibility = getFieldVisibility(args),
    maxMethodVisibility = getMethodVisibility(args),
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
 * -p, --packages: `:` separated packages
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
        writeText(
            ClassDiagram(clazz, scanPackages = getPackages(args), configuration = toConfiguration(args)).plantUml()
        )
    }
}
