package com.z1.comparaprecos.feature.listacompra.presentation.viewmodel

import com.z1.comparaprecos.common.util.UiEvent
import com.z1.comparaprecos.core.model.ListaCompra
import com.z1.comparaprecos.core.model.ListaCompraWithProdutos

sealed class OnEvent {
    data class Insert(val novaListaCompra: ListaCompra): OnEvent()
    data class Delete(val idListaCompra: Long): OnEvent()
    data class UpdateTituloListaCompra(val titulo: String): OnEvent()
    data class ListaCompraSelecionada(val listaCompra: ListaCompraWithProdutos?): OnEvent()
    data object Reset: OnEvent()
    data class UpdateUiEvent(val uiEvent: UiEvent): OnEvent()
}
