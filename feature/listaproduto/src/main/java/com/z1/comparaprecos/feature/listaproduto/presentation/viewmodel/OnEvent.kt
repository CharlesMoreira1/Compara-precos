package com.z1.comparaprecos.feature.listaproduto.presentation.viewmodel

import com.z1.comparaprecos.common.util.UiEvent
import com.z1.comparaprecos.core.model.Produto

sealed class OnEvent {
    data class GetListaCompra(val idListaCompra: Long): OnEvent()
    data class InsertProduto(val produto: Produto): OnEvent()
    data class UpdateProduto(val produto: Produto): OnEvent()
    data class DeleteProduto(val produto: Produto): OnEvent()
    data class ProdutoSelecionado(val produto: Produto?): OnEvent()
    data class UpdateQuantidadeProdutoExistente(val produto: Produto?): OnEvent()
    data class UpdateUiEvent(val uiEvent: UiEvent): OnEvent()
}
