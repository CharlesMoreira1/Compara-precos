package com.z1.comparaprecos.feature.novalista.domain

import com.z1.comparaprecos.core.database.repository.produto.ProdutoRepository
import com.z1.comparaprecos.core.model.ListaCompraWithProdutos
import com.z1.comparaprecos.core.model.Produto
import kotlinx.coroutines.flow.Flow

class ProdutoUseCaseImpl(
    private val produtoRepository: ProdutoRepository
): ProdutoUseCase {
    override suspend fun getListaCompra(idListaCompra: Long) =
        produtoRepository.getListaCompra(idListaCompra)

    override suspend fun getListaProduto(): Flow<List<Produto>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertProduto(novoProduto: Produto) =
        produtoRepository.insertProduto(novoProduto)

    override suspend fun editProduto(produto: Produto): Int {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProduto(produto: Produto): Int {
        TODO("Not yet implemented")
    }
}