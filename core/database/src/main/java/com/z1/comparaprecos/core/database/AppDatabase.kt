package com.z1.comparaprecos.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.z1.comparaprecos.core.database.dao.ListaCompraDao
import com.z1.comparaprecos.core.database.dao.ProdutoDao
import com.z1.comparaprecos.core.database.model.ListaCompraEntity
import com.z1.comparaprecos.core.database.model.ProdutoEntity

@Database(
    entities = [ListaCompraEntity::class, ProdutoEntity::class],
    version = 2,
    autoMigrations = [] //DatabaseAutoMigration class
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getListaCompraDao() : ListaCompraDao
    abstract fun getprodutoDao() : ProdutoDao
}