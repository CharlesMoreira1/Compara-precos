package com.z1.comparaprecos.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.z1.comparaprecos.core.database.model.ListaCompraWithProdutosEntity
import com.z1.comparaprecos.core.database.model.ProdutoEntity
import com.z1.comparaprecos.core.model.Produto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProdutoDao {

    @Query("SELECT * FROM tb_lista_compra WHERE id == :idListaCompra")
    suspend fun getListaCompra(idListaCompra: Long): ListaCompraWithProdutosEntity

    @Query("SELECT * FROM tb_produtos")
    fun getListaProduto(): Flow<List<ProdutoEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProduto(novoProduto: ProdutoEntity): Long

    @Update
    suspend fun editProduto(produto: ProdutoEntity): Int

    @Delete
    suspend fun deleteProduto(produto: ProdutoEntity): Int
}