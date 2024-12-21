package io.github.kelvindev15.kotlin2plantuml.test

import io.github.kelvindev15.kotlin2plantuml.plantuml.ClassDiagram
import io.github.kelvindev15.kotlin2plantuml.plantuml.Configuration
import io.github.kelvindev15.kotlin2plantuml.test.sample.Vehicle
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlin.reflect.KVisibility

class RegressionTest : FunSpec() {
    private fun noCarriageReturn(text: String) = text.replace("\r", "")

    private fun comparePlantUml(
        actual: String,
        plantUmlFilePath: String,
    ) = noCarriageReturn(actual) shouldBe noCarriageReturn(ClassLoader.getSystemResource(plantUmlFilePath).readText())

    init {
        test("default configuration") {
            comparePlantUml(ClassDiagram(Vehicle::class).plantUml(), "default_configuration.plantuml")
        }

        test("no recurse") {
            comparePlantUml(
                ClassDiagram(Vehicle::class, configuration = Configuration(recurse = false)).plantUml(),
                "no_recurse.plantuml",
            )
        }

        test("hide methods") {
            comparePlantUml(
                ClassDiagram(Vehicle::class, configuration = Configuration(hideMethods = true)).plantUml(),
                "hide_methods.plantuml",
            )
        }

        test("hide relationships") {
            comparePlantUml(
                ClassDiagram(Vehicle::class, configuration = Configuration(hideRelationships = true)).plantUml(),
                "hide_relationships.plantuml",
            )
        }

        test("hide fields") {
            comparePlantUml(
                ClassDiagram(Vehicle::class, configuration = Configuration(hideFields = true)).plantUml(),
                "hide_fields.plantuml",
            )
        }

        test("display private methods") {
            comparePlantUml(
                ClassDiagram(
                    Vehicle::class,
                    configuration = Configuration(maxMethodVisibility = KVisibility.PRIVATE),
                ).plantUml(),
                "private_methods.plantuml",
            )
        }

        test("display private fields") {
            comparePlantUml(
                ClassDiagram(
                    Vehicle::class,
                    configuration = Configuration(maxFieldVisibility = KVisibility.PRIVATE),
                ).plantUml(),
                "private_fields.plantuml",
            )
        }
    }
}
