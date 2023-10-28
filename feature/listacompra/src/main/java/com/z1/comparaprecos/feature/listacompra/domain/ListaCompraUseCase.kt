package com.z1.comparaprecos.feature.listacompra.domain

import com.z1.comparaprecos.core.model.ListaCompra
import com.z1.comparaprecos.core.model.ListaCompraWithProdutos
import com.z1.comparaprecos.core.model.Produto
import kotlinx.coroutines.flow.Flow

interface ListaCompraUseCase {
    suspend fun getListaCompraWithProdutos(): Flow<List<ListaCompraWithProdutos>>
    suspend fun insertNovaListaCompra(novaListaCompra: ListaCompra): Int
    suspend fun duplicateListaCompra(novaListaCompra: ListaCompra, listaProduto: List<Produto>): Int
    suspend fun updateListaCompra(listaCompra: ListaCompra): Int
    suspend fun deleteListaCompra(idListaCompra: Long): Int

}