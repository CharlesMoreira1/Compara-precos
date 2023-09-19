package com.z1.comparaprecos.core.database.repository.produto

import com.z1.comparaprecos.core.database.dao.ProdutoDao
import com.z1.comparaprecos.core.database.mapper.ListaCompraMapper
import com.z1.comparaprecos.core.database.mapper.ListaCompraWithProdutosMapper
import com.z1.comparaprecos.core.database.mapper.ProdutoMapper
import com.z1.comparaprecos.core.database.model.ListaCompraWithProdutosEntity
import com.z1.comparaprecos.core.database.model.ProdutoEntity
import com.z1.comparaprecos.core.model.ListaCompraWithProdutos
import com.z1.comparaprecos.core.model.Produto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProdutoRepositoryImpl @Inject constructor(
    private val produtoDao: ProdutoDao,
    private val listaCompraMapper: ListaCompraMapper,
    private val produtoMapper: ProdutoMapper,
    private val listaCompraWithProdutosMapper: ListaCompraWithProdutosMapper
): ProdutoRepository {
    override suspend fun getListaCompra(idListaCompra: Long) =
        listaCompraWithProdutosMapper.mapEntityToModel(produtoDao.getListaCompra(idListaCompra))

    override fun getListaProduto() =
        produtoDao.getListaProduto().map { produtoMapper.mapEntityListToModelList(it) }

    override suspend fun insertProduto(novoProduto: Produto) =
        produtoDao.insertProduto(produtoMapper.mapModelToEntity(novoProduto))

    override suspend fun editProduto(produto: Produto): Int {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProduto(produto: Produto): Int {
        TODO("Not yet implemented")
    }
}