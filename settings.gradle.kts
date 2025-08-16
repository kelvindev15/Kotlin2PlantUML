rootProject.name = "Kotlin2PlantUML"

plugins {
    id("org.danilopianini.gradle-pre-commit-git-hooks") version "2.0.30"
}

gitHooks {
    commitMsg { conventionalCommits() }
    preCommit {
        tasks("ktlintCheck")
    }
    createHooks()
}
