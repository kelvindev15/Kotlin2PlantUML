import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.qa)
    alias(libs.plugins.publicOnCentral)
    alias(libs.plugins.dokka)
    alias(libs.plugins.shadowJar)
    alias(libs.plugins.semanticVersioning)
    java
    application
}

group = "io.github.kelvindev15"

application {
    mainClass.set("$group.kotlin2plantuml.MainKt")
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots")
        content {
            includeGroup("no.tornado")
        }
    }
}

dependencies {
    implementation(libs.bundles.kotlin)
    api(libs.jGraph)
    api(libs.classgraph)
    implementation(libs.common.cli)
    /* Test */
    testImplementation(libs.bundles.kotest)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
}

val githubUser = "Kelvindev15"

if (System.getenv("CI") == true.toString()) {
    signing {
        val signingKey: String? by project
        val signingPassword: String? by project
        useInMemoryPgpKeys(signingKey, signingPassword)
    }
}

gitSemVer {
    minimumVersion.set("0.1.0")
    developmentIdentifier.set("dev")
    noTagIdentifier.set("archeo")
    fullHash.set(false) // set to true if you want to use the full git hash
    maxVersionLength.set(Int.MAX_VALUE) // Useful to limit the maximum version length, e.g. Gradle Plugins have a limit on 20
    developmentCounterLength.set(2) // How many digits after `dev`
    enforceSemanticVersioning.set(true) // Whether the plugin should stop if the resulting version is not a valid SemVer, or just warn
    preReleaseSeparator.set("-")
    buildMetadataSeparator.set("+")
    distanceCounterRadix.set(36) // The radix for the commit-distance counter. Must be in the 2-36 range.
    versionPrefix.set("")
    assignGitSemanticVersion()
}

publishOnCentral {
    configureMavenCentral.set(true)
    projectDescription.set("A kotlin library for generating plantuml from kotlin code.")
    projectLongName.set(project.name)
    licenseName.set("Apache License, Version 2.0")
    licenseUrl.set("http://www.apache.org/licenses/LICENSE-2.0")
    projectUrl.set("https://github.com/$githubUser/${project.name}")
    scmConnection.set("git:git@github.com:$githubUser/${project.name}")
    repository("https://maven.pkg.github.com/$githubUser/${project.name}", "GitHub") {
        user.set("kelvin-olaiya")
        password.set(System.getenv("GITHUB_TOKEN"))
    }
}

/*
 * Developers and contributors must be added manually
 */
publishing {
    publications {
        withType<MavenPublication> {
            pom {
                developers {
                    developer {
                        name.set("Kelvin Olaiya")
                        email.set("kelvinoluwada.olaiya@studio.unibo.it")
                        url.set("https://kelvin-olaiya.github.io")
                    }
                }
            }
        }
    }
}

tasks.withType<ShadowJar>() {
    archiveBaseName.set("kotlin2plantuml")
    archiveClassifier.set("")
    archiveVersion.set("")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
