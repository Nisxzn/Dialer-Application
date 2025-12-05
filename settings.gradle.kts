pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "smartdialer_mvp"
include(":app")
