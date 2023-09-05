package com.z1.comparaprecos.core.database.repository.listacompra

import com.z1.comparaprecos.core.database.dao.ListaCompraDao
import com.z1.comparaprecos.core.database.mapper.ListaCompraMapper
import com.z1.comparaprecos.core.model.ListaCompra
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ListaCompraRepositoryImpl @Inject constructor(
    private val listaCompraDao: ListaCompraDao,
    private val listaCompraMapper: ListaCompraMapper
): ListaCompraRepository {
    override suspend fun getListaCompra(): Flow<List<ListaCompra>> =
        listaCompraDao.getListaCompra().map { listaCompraMapper.mapEntityListToModelList(it) }

    override suspend fun insertListaCompra(novaListaCompra: ListaCompra): Long =
        listaCompraDao.insertListaCompra(listaCompraMapper.mapModelToEntity(novaListaCompra))

    override suspend fun deleteListaCompra(idListaCompra: Long): Int =
        listaCompraDao.deleteListaCompra(idListaCompra)
}