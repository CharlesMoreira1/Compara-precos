plugins {
    id("comparaprecos.android.feature")
    id("comparaprecos.android.library.compose")
}

android {
    namespace = "com.z1.comparaprecos.core.navigation"
}

dependencies {
    implementation(project(":feature:onboarding"))
    implementation(project(":feature:listacompra"))
    implementation(project(":feature:listaproduto"))
}