plugins {
    id("comparaprecos.android.feature")
    id("comparaprecos.android.room")
}

android {
    namespace = "com.z1.comparaprecos.feature.listacompra"
}

dependencies {
    api(project(":core:model"))
}