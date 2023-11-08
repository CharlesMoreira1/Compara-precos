plugins {
    id("comparaprecos.android.feature")
    id("comparaprecos.android.library.compose")
}

android {
    namespace = "com.z1.comparaprecos.feature.listaproduto"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:database"))
    implementation(project(":core:testing"))
//    implementation(project(":core:model"))
}