package com.z1.comparaprecos.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.z1.comparaprecos.core.database.dao.ListaCompraDao
import com.z1.comparaprecos.core.database.model.ListaCompraEntity

@Database(
    entities = [ListaCompraEntity::class],
    version = 1,
    autoMigrations = []
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getListaCompraDao() : ListaCompraDao
}