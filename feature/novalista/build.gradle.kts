plugins {
    id("comparaprecos.android.feature")
    id("comparaprecos.android.library.compose")
}

android {
    namespace = "com.z1.comparaprecos.feature.novalista"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:database"))
    implementation(project(":core:model"))
}