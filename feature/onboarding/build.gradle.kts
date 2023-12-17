plugins {
    id("comparaprecos.android.feature")
    id("comparaprecos.android.library.compose")
    id("comparaprecos.android.library.jacoco")
}

android {
    namespace = "com.z1.feature.onboarding"
}

dependencies {

    implementation(project(":core:common"))
    implementation(project(":core:datastore"))
//    implementation(project(":core:navigation"))
    implementation(project(":core:testing"))
}