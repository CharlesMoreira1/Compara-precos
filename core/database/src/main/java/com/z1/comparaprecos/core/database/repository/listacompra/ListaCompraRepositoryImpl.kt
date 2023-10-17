package com.z1.comparaprecos.core.database.repository.listacompra

import androidx.room.withTransaction
import com.z1.comparaprecos.core.database.AppDatabase
import com.z1.comparaprecos.core.database.dao.ListaCompraDao
import com.z1.comparaprecos.core.database.dao.ProdutoDao
import com.z1.comparaprecos.core.database.mapper.ListaCompraMapper
import com.z1.comparaprecos.core.database.mapper.ListaCompraWithProdutosMapper
import com.z1.comparaprecos.core.database.mapper.ProdutoMapper
import com.z1.comparaprecos.core.model.ListaCompra
import com.z1.comparaprecos.core.model.Produto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ListaCompraRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase,
    private val listaCompraDao: ListaCompraDao,
    private val produtoDao: ProdutoDao,
    private val listaCompraMapper: ListaCompraMapper,
    private val produtoMapper: ProdutoMapper,
    private val listaCompraWithProdutosMapper: ListaCompraWithProdutosMapper
): ListaCompraRepository {
    override suspend fun getListaCompra(): Flow<List<ListaCompra>> =
        listaCompraDao.getListaCompra().map { listaCompraMapper.mapEntityListToModelList(it) }

    override suspend fun getListaCompraWithProdutos() =
        listaCompraDao.getListaCompraWithProdutos().map { listaCompraWithProdutosMapper.mapEntityListToModelList(it) }

    override suspend fun insertListaCompra(novaListaCompra: ListaCompra): Long =
        listaCompraDao.insertListaCompra(listaCompraMapper.mapModelToEntity(novaListaCompra))

    override suspend fun insertListaCompraAndListaProduto(listaCompra: ListaCompra, listaProduto: List<Produto>) =
        try {
            appDatabase.withTransaction {
                val idListaCompra =
                    listaCompraDao.insertListaCompra(listaCompraMapper.mapModelToEntity(listaCompra))
                val novaListaProduto =
                    listaProduto.map { it.copy(id = 0, idListaCompra = idListaCompra) }
                produtoDao.insertListaProduto(
                    produtoMapper.mapModelListToEntityList(
                        novaListaProduto
                    )
                )
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }

    override suspend fun updateListaCompra(listaCompra: ListaCompra) =
        listaCompraDao.updateListaCompra(listaCompraMapper.mapModelToEntity(listaCompra))

    override suspend fun deleteListaCompra(idListaCompra: Long): Int =
        listaCompraDao.deleteListaCompra(idListaCompra)
}