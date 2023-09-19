package com.z1.comparaprecos.feature.listacompra.domain

import com.z1.comparaprecos.core.model.ListaCompra
import com.z1.comparaprecos.core.model.ListaCompraWithProdutos
import kotlinx.coroutines.flow.Flow

interface ListaCompraUseCase {
    suspend fun getListaCompra(): Flow<List<ListaCompra>>
    suspend fun getListaCompraWithProdutos(): Flow<List<ListaCompraWithProdutos>>
    suspend fun insertNovaCompra(novaListaCompra: ListaCompra): Long
    suspend fun deleteCompra(idListaCompra: Long): Int

}