plugins {
    id("comparaprecos.android.feature")
    id("comparaprecos.android.room")
    id("comparaprecos.android.library.jacoco")
}

android {
    namespace = "com.z1.comparaprecos.core.database"
}

dependencies {
    api(project(":core:model"))
    implementation(project(":core:testing"))
}