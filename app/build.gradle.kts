plugins {
    id("comparaprecos.android.application")
    id("comparaprecos.android.application.compose")
    id("comparaprecos.android.application.jacoco")
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

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            merges += "META-INF/LICENSE.md"
            merges += "META-INF/LICENSE-notice.md"
        }
    }
}

dependencies {
    implementation(libs.bundles.androidx)
    implementation(libs.bundles.compose)

    implementation(project(":core:common"))
    implementation(project(":core:navigation"))
    implementation(project(":core:testing"))
    implementation(project(":feature:listacompra"))
    implementation(project(":feature:listaproduto"))
}