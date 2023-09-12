package com.z1.comparaprecos.feature.listacompra.presentation

import com.z1.comparaprecos.common.ui.components.Mensagem
import com.z1.comparaprecos.core.model.ListaCompra
import com.z1.comparaprecos.core.model.ListaCompraWithProdutos

enum class ELoanding {
    CRIANDO_COMPRA,
    AGUARDE,
    NOTHING
}

enum class EStatusListaCompra {
    CRIADA,
    ERRO_AO_CRIAR,
    EXCLUIDA,
    ERRO_AO_EXCLUIR,
    EDITADA,
    ERRO_AO_EDITAR,
    NOTHING,
}

enum class ETypeErrors {
    TITULO_VAZIO,
    LISTA_TO_COMPARAR_VAZIA,
    ERRO_CARREGAR_LISTA,
    ERRO_INSERIR_LISTA
}
data class UiState(
    var isLoading: ELoanding = ELoanding.NOTHING,
    var isError: Pair<ETypeErrors?, String> = null to "",
    var isErrorTitulo: Boolean = false,
    var titulo: String = "",
    var compararListaCompra: Boolean = false,
    var idListaToComparar: Long = -1,
    var statusListaCompra: EStatusListaCompra = EStatusListaCompra.NOTHING,
    var listaCompra: List<ListaCompraWithProdutos> = emptyList(),
    var listaCompraSelecionada: ListaCompraWithProdutos? = null,
    var listaMensagem: MutableList<Mensagem> = mutableListOf(),
    var isShowingSnackBar: Boolean = false
)