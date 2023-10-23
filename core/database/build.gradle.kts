plugins {
    id("comparaprecos.android.feature")
    id("comparaprecos.android.room")
}

android {
    namespace = "com.z1.comparaprecos.core.database"
}

dependencies {
    api(project(":core:model"))
}