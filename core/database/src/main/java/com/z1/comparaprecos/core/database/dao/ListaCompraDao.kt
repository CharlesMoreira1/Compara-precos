package com.z1.comparaprecos.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.z1.comparaprecos.core.database.model.ListaCompraEntity
import com.z1.comparaprecos.core.database.model.ListaCompraWithProdutosEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ListaCompraDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListaCompra(novaListaCompra: ListaCompraEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateListaCompra(listaCompra: ListaCompraEntity): Int

    @Query("SELECT * FROM tb_lista_compra ORDER BY data_criacao DESC")
    fun getListaCompra(): Flow<List<ListaCompraEntity>>

    @Transaction
    @Query("SELECT * FROM tb_lista_compra ORDER BY data_criacao DESC")
    fun getListaCompraWithProdutos(): Flow<List<ListaCompraWithProdutosEntity>>

    @Query("DELETE FROM tb_lista_compra WHERE id == :idListaCompra")
    suspend fun deleteListaCompra(idListaCompra: Long): Int
}