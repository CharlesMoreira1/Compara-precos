package com.z1.comparaprecos.feature.listacompra.domain

import com.z1.comparaprecos.core.common.R
import com.z1.comparaprecos.core.database.repository.listacompra.ListaCompraRepository
import com.z1.comparaprecos.core.model.ListaCompra
import com.z1.comparaprecos.core.model.ListaCompraWithProdutos
import com.z1.comparaprecos.core.model.Produto
import com.z1.comparaprecos.core.model.exceptions.ErrorDelete
import com.z1.comparaprecos.core.model.exceptions.ErrorEmptyTitle
import com.z1.comparaprecos.core.model.exceptions.ErrorInsert
import com.z1.comparaprecos.core.model.exceptions.ErrorUpdate
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ListaCompraUseCaseImpl @Inject constructor(
    private val listaCompraRepository: ListaCompraRepository
) : ListaCompraUseCase {

    override suspend fun getListaCompraWithProdutos(): Flow<List<ListaCompraWithProdutos>> =
        listaCompraRepository.getListaCompraWithProdutos()

    override suspend fun insertNovaListaCompra(novaListaCompra: ListaCompra): Int {
        if (novaListaCompra.isTituloVazio()) throw ErrorEmptyTitle()
        val isInserted = listaCompraRepository.insertListaCompra(novaListaCompra) > 0
        return if (isInserted) R.string.label_desc_lista_compra_criada
        else throw ErrorInsert()
    }

    override suspend fun duplicateListaCompra(
        novaListaCompra: ListaCompra,
        listaProduto: List<Produto>
    ) = try {
        val isDuplicated =
            listaCompraRepository.insertListaCompraAndListaProduto(novaListaCompra, listaProduto)
        if (isDuplicated) R.string.label_lista_compra_duplicada
        else R.string.label_desc_erro_criar_lista
    } catch (e: Exception) {
        e.printStackTrace()
        R.string.label_desc_erro_criar_lista
    }

    override suspend fun updateListaCompra(listaCompra: ListaCompra): Int {
        val isUpdated = listaCompraRepository.updateListaCompra(listaCompra) > 0
        return if (isUpdated) R.string.label_nome_lista_atualizado
        else throw ErrorUpdate()
    }


    override suspend fun deleteListaCompra(idListaCompra: Long): Int {
        val isDeleted = listaCompraRepository.deleteListaCompra(idListaCompra) > 0
        return if (isDeleted) R.string.label_lista_compra_excluida
        else throw ErrorDelete()
    }
}