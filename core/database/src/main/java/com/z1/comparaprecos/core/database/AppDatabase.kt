package com.z1.comparaprecos.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.z1.comparaprecos.core.database.dao.ListaCompraDao
import com.z1.comparaprecos.core.database.model.ListaCompraEntity
import com.z1.comparaprecos.core.database.model.ProdutoEntity

@Database(
    entities = [ListaCompraEntity::class, ProdutoEntity::class],
    version = 1,
    autoMigrations = []
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getListaCompraDao() : ListaCompraDao
}