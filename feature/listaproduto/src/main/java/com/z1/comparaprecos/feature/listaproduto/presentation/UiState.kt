package com.z1.comparaprecos.feature.listaproduto.presentation

import com.z1.comparaprecos.core.model.ListaCompra
import com.z1.comparaprecos.core.model.ListaCompraWithProdutos
import com.z1.comparaprecos.core.model.Produto
import java.math.BigDecimal

data class UiState(
    val isListaProdutoCarregada: Boolean = false,
    val produtoSelecionado: Produto? = null, //Produto(-1, -1, "", "0.0", BigDecimal.ZERO, "un"),
    val listaCompra: ListaCompra = ListaCompra(-1, "", false, -2, 0L),
    val listaProduto: List<Produto> = emptyList(),
    val listaCompraComparada: ListaCompraWithProdutos = ListaCompraWithProdutos(listaCompra, emptyList())
)