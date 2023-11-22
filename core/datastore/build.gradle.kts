plugins {
    id("comparaprecos.android.library")
    id("comparaprecos.android.hilt")
    id("comparaprecos.android.library.jacoco")
}

android {
    namespace = "com.z1.core.datastore"
}

dependencies {
    implementation(libs.androidx.datastore)
    implementation(project(":core:model"))
}