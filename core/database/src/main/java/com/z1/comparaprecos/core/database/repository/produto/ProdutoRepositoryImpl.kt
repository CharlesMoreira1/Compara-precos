package com.z1.comparaprecos.core.database.repository.produto

import com.z1.comparaprecos.core.database.dao.ProdutoDao
import com.z1.comparaprecos.core.database.mapper.ListaCompraMapper
import com.z1.comparaprecos.core.database.mapper.ListaCompraWithProdutosMapper
import com.z1.comparaprecos.core.database.mapper.ProdutoMapper
import com.z1.comparaprecos.core.model.Produto
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProdutoRepositoryImpl @Inject constructor(
    private val produtoDao: ProdutoDao,
    private val listaCompraMapper: ListaCompraMapper,
    private val produtoMapper: ProdutoMapper,
    private val listaCompraWithProdutosMapper: ListaCompraWithProdutosMapper
): ProdutoRepository {
    override suspend fun getListaCompra(idListaCompra: Long) =
        listaCompraMapper.mapEntityToModel(produtoDao.getListaCompra(idListaCompra))

    override suspend fun getListaCompraComparada(idListaCompra: Long) =
        listaCompraWithProdutosMapper.mapEntityToModel(produtoDao.getListaCompraComparada(idListaCompra))


    override fun getListaProduto(idListaCompra: Long) =
        produtoDao.getListaProduto(idListaCompra).map { produtoMapper.mapEntityListToModelList(it) }

    override suspend fun insertProduto(novoProduto: Produto) =
        produtoDao.insertProduto(produtoMapper.mapModelToEntity(novoProduto))

    override suspend fun updateProduto(produto: Produto) =
        produtoDao.updateProduto(produtoMapper.mapModelToEntity(produto))

    override suspend fun deleteProduto(produto: Produto) =
        produtoDao.deleteProduto(produtoMapper.mapModelToEntity(produto))
}