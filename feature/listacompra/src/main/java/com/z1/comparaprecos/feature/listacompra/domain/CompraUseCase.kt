package com.z1.comparaprecos.feature.listacompra.domain

import com.z1.comparaprecos.core.model.ListaCompra
import kotlinx.coroutines.flow.Flow

interface CompraUseCase {
    suspend fun getListaCompra(): Flow<List<ListaCompra>>
    suspend fun insertNovaCompra(novaListaCompra: ListaCompra): Long
    suspend fun deleteCompra(idListaCompra: Long): Int

}