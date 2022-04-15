import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

// TODO: Use toml file for dependencies
plugins {
    kotlin("jvm") version "1.6.10"
    id("org.danilopianini.gradle-kotlin-qa") version "0.13.0"
    id("org.danilopianini.publish-on-central") version "0.7.17"
    id("org.jetbrains.dokka") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    java
    application
}

group = "io.github.kelvindev15"
version = "1.0.0"

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

val kotestVersion = "5.2.1"
dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.10")
    implementation("org.jgrapht:jgrapht-core:1.5.1")
    implementation("io.github.classgraph:classgraph:4.8.145")
    implementation("commons-cli:commons-cli:1.5.0")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-property:$kotestVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

val githubUser = "Kelvindev15"

if (System.getenv("CI") == true.toString()) {
    signing {
        val signingKey: String? by project
        val signingPassword: String? by project
        useInMemoryPgpKeys(signingKey, signingPassword)
    }
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
        user.set(System.getenv("GITHUB_USERNAME"))
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
