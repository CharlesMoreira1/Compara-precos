package com.z1.comparaprecos.feature.listacompra.presentation.viewmodel

import com.z1.comparaprecos.core.model.ListaCompra
import com.z1.comparaprecos.core.model.ListaCompraWithProdutos
import com.z1.comparaprecos.core.model.Produto
import com.z1.comparaprecos.feature.listacompra.presentation.UiEvent

sealed class OnEvent {
    data class Insert(val novaListaCompra: ListaCompra): OnEvent()
    data class DuplicateListaCompra(val novaListaCompra: ListaCompra, val listaProdutoListaCompraSelecionada: List<Produto>): OnEvent()
    data class UpdateListaCompra(val novaListaCompra: ListaCompra): OnEvent()
    data class Delete(val idListaCompra: Long): OnEvent()
    data object UiCreateNewListaCompra: OnEvent()
    data object UiDuplicateListaCompra: OnEvent()
    data object UiRenameListaCompra: OnEvent()
    data class ListaCompraSelecionada(val listaCompra: ListaCompraWithProdutos?): OnEvent()
    data class UpdateTituloListaCompra(val titulo: String): OnEvent()
    data object Reset: OnEvent()
    data class UpdateUiEvent(val uiEvent: UiEvent): OnEvent()
}
