package com.z1.comparaprecos.feature.listacompra.presentation.viewmodel

import com.z1.comparaprecos.core.model.ListaCompra
import com.z1.comparaprecos.core.model.ListaCompraWithProdutos
import com.z1.comparaprecos.core.model.Produto
import com.z1.comparaprecos.feature.listacompra.presentation.UiEvent

sealed class OnEvent {
    data class Insert(val novaListaCompra: ListaCompra): OnEvent()
    data class DuplicateList(val novaListaCompra: ListaCompra, val listaProdutoListaCompraSelecionada: List<Produto>): OnEvent()
    data class UpdateList(val novaListaCompra: ListaCompra): OnEvent()
    data class Delete(val idListaCompra: Long): OnEvent()
    data object UiCreateNewList: OnEvent()
    data object UiDuplicateList: OnEvent()
    data object UiRenameList: OnEvent()
    data class ListaCompraSelecionada(val listaCompra: ListaCompraWithProdutos?): OnEvent()
    data class UpdateTituloListaCompra(val titulo: String): OnEvent()
    data object Reset: OnEvent()
    data class UpdateUiEvent(val uiEvent: UiEvent): OnEvent()
}
