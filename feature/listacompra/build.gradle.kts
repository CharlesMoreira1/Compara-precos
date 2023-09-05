plugins {
    id("comparaprecos.android.feature")
    id("comparaprecos.android.library.compose")

}

android {
    namespace = "com.z1.comparaprecos.feature.listacompra"

    android {

//        compileSdk = 33
//        defaultConfig {
//            minSdk = 26
//            targetSdk = 33
//
//        }
    }
}

//kapt {
//    correctErrorTypes = true
//}

dependencies {

//    kapt(Libs.kaptDaggerHilt)
//    kapt(Libs.kaptRoom)
//
//    implementation(Libs.daggerHilt)

    implementation(project(":core:common"))
    implementation(project(":core:database"))
    implementation(project(":core:model"))
}