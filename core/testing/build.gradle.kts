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
    debugApi(libs.bundles.debug.test)
}