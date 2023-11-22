@file:Suppress("UnstableApiUsage")

include(":core:datastore")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
    }
}

rootProject.name = "Compara Precos"
include(":app")
include(":core:common")
include(":core:testing")
include(":feature:listacompra")
include(":core:navigation")
include(":feature:listaproduto")
include(":core:model")
include(":core:database")
