package com.z1.comparaprecos.feature.listaproduto.domain

import com.z1.comparaprecos.core.model.ListaCompra
import com.z1.comparaprecos.core.model.ListaCompraWithProdutos
import com.z1.comparaprecos.core.model.Produto
import kotlinx.coroutines.flow.Flow

interface ProdutoUseCase {
    suspend fun insertProduto(novoProduto: Produto, listaProduto: List<Produto>): Int
    suspend fun getListaCompra(idListaCompra: Long): ListaCompra
    suspend fun getListaCompraOptions(idListaCompraAtual: Long): List<Pair<String, Long>>
    suspend fun getListaCompraComparada(idListaCompra: Long): ListaCompraWithProdutos
    suspend fun getListaProduto(idListaCompra: Long): Flow<List<Produto>>
    suspend fun updateProduto(produto: Produto): Int
    suspend fun deleteProduto(produto: Produto): Int
}