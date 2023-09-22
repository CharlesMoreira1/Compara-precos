package com.z1.comparaprecos.feature.novalista.domain

import com.z1.comparaprecos.core.database.repository.produto.ProdutoRepository
import com.z1.comparaprecos.core.model.Produto

class ProdutoUseCaseImpl(
    private val produtoRepository: ProdutoRepository
): ProdutoUseCase {
    override suspend fun getListaCompra(idListaCompra: Long) =
        produtoRepository.getListaCompra(idListaCompra)

    override suspend fun getListaCompraComparada(idListaCompra: Long) =
        produtoRepository.getListaCompraComparada(idListaCompra)

    override suspend fun getListaProduto(idListaCompra: Long) =
        produtoRepository.getListaProduto(idListaCompra)

    override suspend fun insertProduto(novoProduto: Produto) =
        produtoRepository.insertProduto(novoProduto)

    override suspend fun updateProduto(produto: Produto) =
        produtoRepository.updateProduto(produto)

    override suspend fun deleteProduto(produto: Produto) =
        produtoRepository.deleteProduto(produto)
}