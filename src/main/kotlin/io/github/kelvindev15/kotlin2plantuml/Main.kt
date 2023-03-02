package io.github.kelvindev15.kotlin2plantuml

import io.github.kelvindev15.kotlin2plantuml.plantuml.ClassDiagram
import io.github.kelvindev15.kotlin2plantuml.plantuml.Configuration
import io.github.kelvindev15.kotlin2plantuml.utils.DefaultScanConfiguration
import io.github.kelvindev15.kotlin2plantuml.utils.ReflectUtils
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Option
import org.apache.commons.cli.Options
import java.io.File
import kotlin.reflect.KVisibility
import kotlin.system.exitProcess

/**
 * Tries to parse [option] to a [KVisibility].
 */
fun toVisibility(option: String?) = option?.let { KVisibility.values()[it.toInt()] } ?: KVisibility.PUBLIC

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
 * -cp, --classpath: `:` separated paths
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
    val help = Option("h", "help", false, "Display this message")
    val recurse = Option("r", "recurse", false, "Visit class class hierarchy")
    val output = Option("o", "output", true, "Output file path")
    val packages = Option("p", "packages", true, "':' separated packages (for subclasses)")
    val classpath = Option("cp", "classpath", true, "':' separated paths (for classpath)")
    val hideFields = Option("hf", "hide-fields", false, "Hide fields on classes")
    val hideMethods = Option("hm", "hide-methods", false, "Hide methods on classes")
    val hideRelationships = Option("hr", "hide-relationships", false, "Hide relationships between classes")
    val fieldVisibility = Option(
        "fv",
        "field-visibility",
        true,
        "Max. field visibility (0=Public, 1=Protected, 2=Internal, 3=Private)"
    )
    val methodVisibility = Option(
        "mv",
        "method-visibility",
        true,
        "Max. method visibility (0=Public, 1=Protected, 2=Internal, 3=Private)"
    )
    val options = Options()
        .addOption(help)
        .addOption(recurse)
        .addOption(output)
        .addOption(packages)
        .addOption(classpath)
        .addOption(hideFields)
        .addOption(hideMethods)
        .addOption(hideRelationships)
        .addOption(fieldVisibility)
        .addOption(methodVisibility)
    val commandLine = DefaultParser().parse(options, args)
    val configuration = Configuration(
        hideFields = commandLine.hasOption(hideFields),
        hideMethods = commandLine.hasOption(hideMethods),
        hideRelationships = commandLine.hasOption(hideRelationships),
        recurse = commandLine.hasOption(recurse),
        maxFieldVisibility = toVisibility(commandLine.getOptionValue(fieldVisibility)),
        maxMethodVisibility = toVisibility(commandLine.getOptionValue(methodVisibility)),
    )
    if (commandLine.hasOption(help)) {
        HelpFormatter().printHelp("java -jar kotlin2plantuml.jar full.class.name [...options]", options)
        exitProcess(0)
    }
    require(args.isNotEmpty()) {
        "No fully qualified input class had been provided"
    }
    commandLine.getOptionValue(packages)?.split(":")?.forEach { DefaultScanConfiguration.addPackage(it) }
    commandLine.getOptionValue(classpath)?.split(":")?.forEach { DefaultScanConfiguration.addClasspath(it) }
    val outputFile = commandLine.getOptionValue(output)
        ?: "build${File.separatorChar}reports${File.separatorChar}diagram.plantuml"
    val clazz = ReflectUtils.loadClassOrThrow(args[0])
    File(outputFile).apply {
        parentFile?.mkdirs()
        createNewFile()
        writeText(
            ClassDiagram(
                clazz,
                configuration = configuration,
            ).plantUml()
        )
    }
}
