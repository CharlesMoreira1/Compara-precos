package com.z1.comparaprecos.core.database.repository.listacompra

import com.z1.comparaprecos.core.model.ListaCompra
import kotlinx.coroutines.flow.Flow

interface ListaCompraRepository {
    suspend fun getListaCompra(): Flow<List<ListaCompra>>
    suspend fun insertListaCompra(novaListaCompra: ListaCompra): Long
    suspend fun deleteListaCompra(idListaCompra: Long): Int
}