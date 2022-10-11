var publishCmd = `
git tag -a -f \${nextRelease.version} \${nextRelease.version} -F CHANGELOG.md
git push --force origin \${nextRelease.version} || exit 6
./gradlew shadowJar --parallel || ./gradlew shadowJar --parallel || exit 4
./gradlew uploadKotlinOSSRHToMavenCentralNexus releaseStagingRepositoryOnMavenCentral --parallel || exit 5
./gradlew publishAllPublicationsToGitHubRepository --continue || true
`
var config = require('semantic-release-preconfigured-conventional-commits');
config.plugins.push(
    ["@semantic-release/exec", {
        "publishCmd": publishCmd,
    }],
    ["@semantic-release/github", {
        "assets": [
            { "path": "build/libs/*.jar" },
        ]
    }],
    "@semantic-release/git",
)
module.exports = config
