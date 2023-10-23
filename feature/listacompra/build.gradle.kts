plugins {
    id("comparaprecos.android.feature")
    id("comparaprecos.android.library.compose")
    id("comparaprecos.android.library.jacoco")
}

android {
    namespace = "com.z1.comparaprecos.feature.listacompra"
}

dependencies {

    implementation(project(":core:common"))
    implementation(project(":core:database"))
//    implementation(project(":core:model"))
}