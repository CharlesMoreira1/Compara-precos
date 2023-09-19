package com.z1.comparaprecos.feature.novalista.presentation.viewmodel

import com.z1.comparaprecos.core.model.Produto

sealed class OnEvent {
    data class GetListaCompra(val idListaCompra: Long): OnEvent()
    data class InsertProduto(val produto: Produto): OnEvent()
    data class UpdateProduto(val produto: Produto): OnEvent()
    data class DeleteProduto(val produto: Produto): OnEvent()
}
