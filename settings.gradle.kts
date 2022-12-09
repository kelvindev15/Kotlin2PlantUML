rootProject.name = "Kotlin2PlantUML"
enableFeaturePreview("VERSION_CATALOGS")

plugins {
    id("org.danilopianini.gradle-pre-commit-git-hooks") version "1.1.0"
}

gitHooks {
    commitMsg { conventionalCommits() }
    preCommit {
        tasks("ktlintCheck")
    }
    createHooks()
}
