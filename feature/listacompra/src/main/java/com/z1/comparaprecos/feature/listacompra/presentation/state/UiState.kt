package com.z1.comparaprecos.feature.listacompra.presentation.state

import androidx.annotation.StringRes
import com.z1.comparaprecos.common.ui.components.Mensagem
import com.z1.comparaprecos.core.model.ListaCompraWithProdutos
import com.z1.comparaprecos.core.common.R
import com.z1.comparaprecos.core.model.UserData

data class UiState(
    var isTituloVazio: Boolean = false,
    var isDuplicarListaCompra: Boolean = false,
    var isRenomearListaCompra: Boolean = false,
    var tituloListaCompra: String = "",
    var isListaCompraCarregada: Boolean = false,
    var listaCompra: List<ListaCompraWithProdutos> = emptyList(),
    var listaCompraSelecionada: ListaCompraWithProdutos? = null,
    var listaMensagem: MutableList<Mensagem> = mutableListOf(),
    var userData: UserData = UserData(),

    @StringRes var tituloBottomSheet: Int = R.string.label_nova_lista_compra,
    @StringRes var descricaoBottomSheet: Int = R.string.label_desc_nova_lista_compra,
    @StringRes var buttonBottomSheet: Int = R.string.label_criar_lista_compra
)