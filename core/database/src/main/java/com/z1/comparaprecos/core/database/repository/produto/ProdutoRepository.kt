package com.z1.comparaprecos.core.database.repository.produto

import com.z1.comparaprecos.core.model.ListaCompra
import com.z1.comparaprecos.core.model.ListaCompraWithProdutos
import com.z1.comparaprecos.core.model.Produto
import kotlinx.coroutines.flow.Flow

interface ProdutoRepository {
    suspend fun getListaCompra(idListaCompra: Long): ListaCompra
    suspend fun getListaCompraComparada(idListaCompra: Long): ListaCompraWithProdutos
    fun getListaProduto(idListaCompra: Long): Flow<List<Produto>>
    suspend fun insertProduto(novoProduto: Produto): Long
    suspend fun updateProduto(produto: Produto): Int
    suspend fun deleteProduto(produto: Produto): Int
}