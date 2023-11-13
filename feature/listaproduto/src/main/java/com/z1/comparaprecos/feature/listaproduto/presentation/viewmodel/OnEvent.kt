package com.z1.comparaprecos.feature.listaproduto.presentation.viewmodel

import com.z1.comparaprecos.core.model.Produto
import com.z1.comparaprecos.feature.listaproduto.presentation.state.UiEvent

sealed class OnEvent {
    data class GetListaCompra(val idListaCompra: Long): OnEvent()
    data class GetListaCompraToComparar(val idListaCompra: Long): OnEvent()
    data object GetListaCompraOptions: OnEvent()
    data class InsertProduto(val produto: Produto): OnEvent()
    data class UpdateProduto(val produto: Produto): OnEvent()
    data class DeleteProduto(val produto: Produto): OnEvent()
    data class ProdutoSelecionado(val produto: Produto?): OnEvent()
    data class UpdateQuantidadeProdutoExistente(val produto: Produto?): OnEvent()
    data class UpdateUiEvent(val uiEvent: UiEvent): OnEvent()
}
