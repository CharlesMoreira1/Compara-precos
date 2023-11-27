package com.z1.comparaprecos.feature.listaproduto.domain

import com.z1.comparaprecos.common.util.ListOrder
import com.z1.comparaprecos.core.common.R
import com.z1.comparaprecos.core.database.model.ProdutoEntity
import com.z1.comparaprecos.core.database.repository.produto.ProdutoRepository
import com.z1.comparaprecos.core.model.Produto
import com.z1.comparaprecos.core.model.exceptions.ErrorDelete
import com.z1.comparaprecos.core.model.exceptions.ErrorEmptyList
import com.z1.comparaprecos.core.model.exceptions.ErrorInsert
import com.z1.comparaprecos.core.model.exceptions.ErrorProductData
import com.z1.comparaprecos.core.model.exceptions.ErrorProductExists
import com.z1.comparaprecos.core.model.exceptions.ErrorUpdate
import kotlinx.coroutines.flow.Flow

class ProdutoUseCaseImpl(
    private val produtoRepository: ProdutoRepository,
): ProdutoUseCase {
    override suspend fun insertProduto(novoProduto: Produto, listaProduto: List<Produto>): Int {
        val productExists = listaProduto.find { it.nomeProduto == novoProduto.nomeProduto } != null
        if (productExists) throw ErrorProductExists()

        val isDadosCorretos = isDadosProdutoCorreto(novoProduto)
        if (!isDadosCorretos.first) throw ErrorProductData(uiMessageId = isDadosCorretos.second)

        val isInserted = produtoRepository.insertProduto(novoProduto) > 0
        return if (isInserted) R.string.label_produto_adicionado
        else throw ErrorInsert()
    }

    override suspend fun getListaCompra(idListaCompra: Long) =
        produtoRepository.getListaCompra(idListaCompra)

    override suspend fun getListaCompraOptions(idListaCompraAtual: Long): List<Pair<String, Long>> {
        val listaCompra = produtoRepository.getAllListaCompra()
        if (listaCompra.isEmpty()) throw ErrorEmptyList()

        return listaCompra.filter {
            it.id != idListaCompraAtual
        }.map {
            it.titulo to it.id
        }
    }

    override suspend fun getListaCompraComparada(idListaCompra: Long) =
        produtoRepository.getListaCompraComparada(idListaCompra)

    override suspend fun getListaProduto(idListaCompra: Long, listOrder: ListOrder): Flow<List<Produto>> {
        val orderBy = when (listOrder) {
            ListOrder.A_Z -> "${ProdutoEntity.COLUMN_NOME_PRODUTO} ASC"
            ListOrder.Z_A -> "${ProdutoEntity.COLUMN_NOME_PRODUTO} DESC"
            ListOrder.ADICIONADO_PRIMEIRO -> "${ProdutoEntity.COLUMN_ID} DESC"
            ListOrder.ADICIONADO_ULTIMO -> "${ProdutoEntity.COLUMN_ID} ASC"
        }
        return produtoRepository.getListaProduto(idListaCompra, orderBy)
    }


    override suspend fun updateProduto(produto: Produto): Int {
        val isUpdated = produtoRepository.updateProduto(produto) > 0
        return if (isUpdated) R.string.label_produto_editado
        else throw ErrorUpdate()
    }

    override suspend fun deleteProduto(produto: Produto): Int {
        val isDeleted = produtoRepository.deleteProduto(produto) > 0
        return if (isDeleted) R.string.label_produto_removido
        else throw ErrorDelete()
    }

    private fun isDadosProdutoCorreto(produto: Produto): Pair<Boolean, Int?> {
        return when {
            produto.nomeProduto.isBlank() -> false to R.string.label_informe_nome_produto
            produto.quantidade.toDouble() <= 0.0 -> false to R.string.label_quantidade_invalida
            else -> true to null
        }
    }
}