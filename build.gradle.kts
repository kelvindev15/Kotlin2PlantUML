// TODO: Use val to uniform kotlin version
plugins {
    kotlin("jvm") version "1.6.10"
    id("org.danilopianini.publish-on-central") version "0.7.14"
    id("org.danilopianini.gradle-kotlin-qa") version "0.13.0"
    java
    application
}

group = "io.github.kelvindev15"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("$group.kotlin2plantuml.MainKt")
}

repositories {
    mavenCentral()
}

val kotestVersion = "5.2.1"
dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.10")
    implementation("org.jgrapht:jgrapht-core:1.5.1")
    implementation("io.github.classgraph:classgraph:4.8.141")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-property:$kotestVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
