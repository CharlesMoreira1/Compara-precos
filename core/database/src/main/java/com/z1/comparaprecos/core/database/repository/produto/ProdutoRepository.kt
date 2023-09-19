package com.z1.comparaprecos.core.database.repository.produto

import com.z1.comparaprecos.core.database.model.ListaCompraWithProdutosEntity
import com.z1.comparaprecos.core.database.model.ProdutoEntity
import com.z1.comparaprecos.core.model.ListaCompraWithProdutos
import com.z1.comparaprecos.core.model.Produto
import kotlinx.coroutines.flow.Flow

interface ProdutoRepository {
    suspend fun getListaCompra(idListaCompra: Long): ListaCompraWithProdutos
    fun getListaProduto(): Flow<List<Produto>>
    suspend fun insertProduto(novoProduto: Produto): Long
    suspend fun editProduto(produto: Produto): Int
    suspend fun deleteProduto(produto: Produto): Int
}