package com.z1.comparaprecos.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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

    @Query("SELECT * FROM tb_produtos WHERE id_lista_compra = :idListaCompra ORDER BY id DESC")
    fun getListaProduto(idListaCompra: Long): Flow<List<ProdutoEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProduto(novoProduto: ProdutoEntity): Long

    @Update
    suspend fun updateProduto(produto: ProdutoEntity): Int

    @Delete
    suspend fun deleteProduto(produto: ProdutoEntity): Int
}