package com.z1.comparaprecos.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.z1.comparaprecos.core.database.model.ListaCompraEntity
import com.z1.comparaprecos.core.database.model.ListaCompraWithProdutosEntity
import com.z1.comparaprecos.core.database.model.ProdutoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProdutoDao {

    @Query("SELECT * FROM tb_lista_compra WHERE id == :idListaCompra")
    suspend fun getListaCompra(idListaCompra: Long): ListaCompraEntity

    @Query("SELECT * FROM tb_lista_compra")
    suspend fun getAllListaCompra(): List<ListaCompraEntity>

    @Query("SELECT * FROM tb_lista_compra WHERE id == :idListaCompra")
    suspend fun getListaCompraComparada(idListaCompra: Long): ListaCompraWithProdutosEntity

    @RawQuery(observedEntities = [ ProdutoEntity::class ])
    fun getListaProduto(query: SupportSQLiteQuery): Flow<List<ProdutoEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProduto(novoProduto: ProdutoEntity): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertListaProduto(listaProduto: List<ProdutoEntity>): List<Long>

    @Update
    suspend fun updateProduto(produto: ProdutoEntity): Int

    @Delete
    suspend fun deleteProduto(produto: ProdutoEntity): Int
}