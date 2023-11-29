rootProject.name = "Kotlin2PlantUML"

plugins {
    id("org.danilopianini.gradle-pre-commit-git-hooks") version "1.1.16"
}

gitHooks {
    commitMsg { conventionalCommits() }
    preCommit {
        tasks("ktlintCheck")
    }
    createHooks()
}
