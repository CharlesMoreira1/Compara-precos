package com.z1.comparaprecos.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.z1.comparaprecos.core.database.model.ListaCompraEntity
import com.z1.comparaprecos.core.database.model.ListaCompraWithProdutosEntity
import com.z1.comparaprecos.core.database.model.ProdutoEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

@Dao
interface ProdutoDao {

    @Query("SELECT * FROM tb_lista_compra WHERE id == :idListaCompra")
    suspend fun getListaCompra(idListaCompra: Long): ListaCompraEntity

    @Query("SELECT * FROM tb_lista_compra")
    suspend fun getAllListaCompra(): List<ListaCompraEntity>

    @Query("")
    @Transaction
    suspend fun getListaCompraComparada(idListaCompra: Long, query: SupportSQLiteQuery): ListaCompraWithProdutosEntity {
        return ListaCompraWithProdutosEntity(
            detalhes = getListaCompra(idListaCompra),
            produtos = getListaProduto(query).first()
        )
    }

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