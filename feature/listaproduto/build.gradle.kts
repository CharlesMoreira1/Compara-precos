plugins {
    id("comparaprecos.android.feature")
    id("comparaprecos.android.library.compose")
}

android {
    namespace = "com.z1.comparaprecos.feature.listaproduto"
}

dependencies {
    api(project(":core:common"))
    api(project(":core:database"))
    api(project(":core:model"))
}