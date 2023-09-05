package com.z1.comparaprecos.feature.listacompra.domain

import com.z1.comparaprecos.core.database.repository.listacompra.ListaCompraRepository
import com.z1.comparaprecos.core.model.ListaCompra
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CompraUseCaseImpl @Inject constructor(
    private val listaCompraRepository: ListaCompraRepository
) : CompraUseCase {

    override suspend fun getListaCompra(): Flow<List<ListaCompra>> {
        return flow {
            try {
                listaCompraRepository.getListaCompra().collect { listaCompra ->
                    emit(listaCompra)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emit(emptyList())
            }
        }
    }

    override suspend fun insertNovaCompra(novaListaCompra: ListaCompra): Long {
        return listaCompraRepository.insertListaCompra(novaListaCompra)
    }

    override suspend fun deleteCompra(idListaCompra: Long): Int {
        return listaCompraRepository.deleteListaCompra(idListaCompra)
    }
}