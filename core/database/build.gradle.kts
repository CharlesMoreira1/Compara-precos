plugins {
    id("comparaprecos.android.feature")
    id("comparaprecos.android.room")
}

android {
    namespace = "com.z1.comparaprecos.feature.listacompra"

    android {
    }
}

dependencies {
    implementation(project(":core:model"))
}