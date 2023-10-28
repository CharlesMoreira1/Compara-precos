plugins {
    id("comparaprecos.android.library")
    id("comparaprecos.android.library.compose")
    id("comparaprecos.android.hilt")
}

android {
    namespace = "com.z1.comparaprecos.testing"
}

dependencies {
    api(libs.bundles.test)
    api(libs.bundles.android.test)
    api(libs.bundles.mocck)
    debugApi(libs.bundles.debug.test)

    implementation(project(":core:database"))
}