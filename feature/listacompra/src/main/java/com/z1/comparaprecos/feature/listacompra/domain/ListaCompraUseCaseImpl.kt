package com.z1.comparaprecos.feature.listacompra.domain

import com.z1.comparaprecos.core.database.repository.listacompra.ListaCompraRepository
import com.z1.comparaprecos.core.model.ListaCompra
import com.z1.comparaprecos.core.model.ListaCompraWithProdutos
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ListaCompraUseCaseImpl @Inject constructor(
    private val listaCompraRepository: ListaCompraRepository
) : ListaCompraUseCase {

    override suspend fun getListaCompra(): Flow<List<ListaCompra>> = listaCompraRepository.getListaCompra()

    override suspend fun getListaCompraWithProdutos(): Flow<List<ListaCompraWithProdutos>> =
        listaCompraRepository.getListaCompraWithProdutos()

    override suspend fun insertNovaCompra(novaListaCompra: ListaCompra): Long {
        return listaCompraRepository.insertListaCompra(novaListaCompra)
    }

    override suspend fun deleteCompra(idListaCompra: Long): Int {
        return listaCompraRepository.deleteListaCompra(idListaCompra)
    }
}