package com.z1.comparaprecos.feature.listacompra.presentation

import com.z1.comparaprecos.common.ui.components.Mensagem
import com.z1.comparaprecos.core.model.ListaCompraWithProdutos

data class UiState(
    var isTituloVazio: Boolean = false,
    var titulo: String = "",
    var compararListaCompra: Boolean = false,
    var idListaToComparar: Long = -1,
    var isListaCompraCarregada: Boolean = false,
    var listaCompra: List<ListaCompraWithProdutos> = emptyList(),
    var listaCompraSelecionada: ListaCompraWithProdutos? = null,
    var listaMensagem: MutableList<Mensagem> = mutableListOf(),
    var isShowingSnackBar: Boolean = false
)