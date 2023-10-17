package com.z1.comparaprecos.core.database.repository.listacompra

import com.z1.comparaprecos.core.model.ListaCompra
import com.z1.comparaprecos.core.model.ListaCompraWithProdutos
import com.z1.comparaprecos.core.model.Produto
import kotlinx.coroutines.flow.Flow

interface ListaCompraRepository {
    suspend fun getListaCompra(): Flow<List<ListaCompra>>
    suspend fun getListaCompraWithProdutos(): Flow<List<ListaCompraWithProdutos>>
    suspend fun insertListaCompra(novaListaCompra: ListaCompra): Long
    suspend fun insertListaCompraAndListaProduto(listaCompra: ListaCompra, listaProduto: List<Produto>): Boolean
    suspend fun updateListaCompra(listaCompra: ListaCompra): Int
    suspend fun deleteListaCompra(idListaCompra: Long): Int
}