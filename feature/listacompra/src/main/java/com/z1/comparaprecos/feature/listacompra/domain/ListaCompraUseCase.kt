package com.z1.comparaprecos.feature.listacompra.domain

import androidx.annotation.StringRes
import com.z1.comparaprecos.common.util.UiText
import com.z1.comparaprecos.core.model.ListaCompra
import com.z1.comparaprecos.core.model.ListaCompraWithProdutos
import com.z1.comparaprecos.core.model.Produto
import com.z1.comparaprecos.feature.listacompra.presentation.UiEvent
import kotlinx.coroutines.flow.Flow

interface ListaCompraUseCase {
    suspend fun getListaCompra(): Flow<List<ListaCompra>>
    suspend fun getListaCompraWithProdutos(): Flow<List<ListaCompraWithProdutos>>
    suspend fun insertNovaListaCompra(novaListaCompra: ListaCompra): Int
    suspend fun duplicateListaCompra(novaListaCompra: ListaCompra, listaProduto: List<Produto>): Int
    suspend fun updateListaCompra(listaCompra: ListaCompra): Int
    suspend fun deleteCompra(idListaCompra: Long): Int

}