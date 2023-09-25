plugins {
    id("comparaprecos.android.feature")
    id("comparaprecos.android.library.compose")
}

android {
    namespace = "com.z1.comparaprecos.core.navigation"

    android {

//        compileSdk = 33
//        defaultConfig {
//            minSdk = 26
//            targetSdk = 33
//
//        }
    }
}

dependencies {
    api(project(":feature:listacompra"))
    api(project(":feature:listaproduto"))
}