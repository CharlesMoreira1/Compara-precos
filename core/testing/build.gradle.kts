plugins {
    id("comparaprecos.android.library")
    id("comparaprecos.android.library.compose")
    id("comparaprecos.android.hilt")
    id("comparaprecos.android.library.jacoco")
}

android {
    namespace = "com.z1.comparaprecos.testing"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    api(libs.bundles.test)
    api(libs.bundles.android.test)
    api(libs.bundles.mocck)
    debugApi(libs.bundles.debug.test)

    implementation(project(":core:model"))
}