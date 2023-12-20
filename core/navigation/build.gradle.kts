plugins {
    id("comparaprecos.android.feature")
    id("comparaprecos.android.library.compose")
}

android {
    namespace = "com.z1.comparaprecos.core.navigation"
}

dependencies {
    api(project(":feature:onboarding"))
    api(project(":feature:listacompra"))
    api(project(":feature:listaproduto"))
}