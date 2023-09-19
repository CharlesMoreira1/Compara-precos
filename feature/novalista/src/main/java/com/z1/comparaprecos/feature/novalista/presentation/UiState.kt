package com.z1.comparaprecos.feature.novalista.presentation

import com.z1.comparaprecos.core.model.ListaCompra
import com.z1.comparaprecos.core.model.ListaCompraWithProdutos
import com.z1.comparaprecos.core.model.Produto
import java.math.BigDecimal

enum class EStatusScreen {
    CARREGANDO_LISTA_COMPRA,
    LISTA_COMPRA,
    LISTA_COMPRA_COMPARADA
}
data class UiState(
    val screen: EStatusScreen = EStatusScreen.CARREGANDO_LISTA_COMPRA,
    val produto: Produto = Produto(-1, -1, "", 0.0, BigDecimal.ZERO),
    val listaCompra: ListaCompraWithProdutos = ListaCompraWithProdutos(ListaCompra(-1, "", false, -2, 0L), emptyList()),
    val listaCompraComparada: ListaCompraWithProdutos = ListaCompraWithProdutos(ListaCompra(-1, "", false, -2, 0L), emptyList())
)