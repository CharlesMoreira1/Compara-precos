plugins {
    id("comparaprecos.android.feature")
    id("comparaprecos.android.library.compose")
}

android {
    namespace = "com.z1.comparaprecos.feature.novalista"
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
    implementation(project(":core:common"))
}