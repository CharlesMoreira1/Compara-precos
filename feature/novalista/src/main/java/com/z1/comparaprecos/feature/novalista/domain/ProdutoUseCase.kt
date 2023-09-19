package com.z1.comparaprecos.feature.novalista.domain

import com.z1.comparaprecos.core.model.ListaCompraWithProdutos
import com.z1.comparaprecos.core.model.Produto
import kotlinx.coroutines.flow.Flow

interface ProdutoUseCase {
    suspend fun getListaCompra(idListaCompra: Long): ListaCompraWithProdutos
    suspend fun getListaProduto(): Flow<List<Produto>>
    suspend fun insertProduto(novoProduto: Produto): Long
    suspend fun editProduto(produto: Produto): Int
    suspend fun deleteProduto(produto: Produto): Int
}