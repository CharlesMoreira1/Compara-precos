import org.gradle.kotlin.dsl.`kotlin-dsl`

plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

group = "com.z1.comparaprecos.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplicationCompose") {
            id = "comparaprecos.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidApplication") {
            id = "comparaprecos.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidApplicationJacoco") {
            id = "comparaprecos.android.application.jacoco"
            implementationClass = "AndroidApplicationJacocoConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "comparaprecos.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = "comparaprecos.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryJacoco") {
            id = "comparaprecos.android.library.jacoco"
            implementationClass = "AndroidLibraryJacocoConventionPlugin"
        }
        register("androidFeature") {
            id = "comparaprecos.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("androidHilt") {
            id = "comparaprecos.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("androidRoom") {
            id = "comparaprecos.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
    }
}