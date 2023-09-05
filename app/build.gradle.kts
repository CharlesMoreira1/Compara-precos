plugins {
    id("comparaprecos.android.application")
    id("comparaprecos.android.application.compose")
    id("comparaprecos.android.hilt")
}

android {
    namespace = "com.z1.comparaprecos"

    defaultConfig {
        applicationId = "com.z1.comparaprecos"
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
}

dependencies {
    implementation(libs.bundles.androidx)
    implementation(libs.bundles.compose)

    testImplementation(libs.bundles.compose)
    androidTestImplementation(libs.bundles.compose)
    debugImplementation(libs.bundles.compose)

    implementation(project(":core:common"))
    implementation(project(":core:database"))
    implementation(project(":core:navigation"))
    implementation(project(":feature:listacompra"))
    implementation(project(":feature:novalista"))
}