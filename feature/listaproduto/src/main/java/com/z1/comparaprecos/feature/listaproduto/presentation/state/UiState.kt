package com.z1.comparaprecos.feature.listaproduto.presentation.state

import com.z1.comparaprecos.core.model.ListaCompra
import com.z1.comparaprecos.core.model.ListaCompraWithProdutos
import com.z1.comparaprecos.core.model.Produto

data class UiState(
    val isListaProdutoCarregada: Boolean = false,
    val produtoSelecionado: Produto? = null,
    val produtoJaExiste: Produto? = null,
    val listaCompra: ListaCompra = ListaCompra(-1, "", 0L),
    val listaCompraOptions: List<Pair<String, Long>> = emptyList(),
    val listaProduto: List<Produto> = emptyList(),
    val listaCompraComparada: ListaCompraWithProdutos = ListaCompraWithProdutos(listaCompra, emptyList())
)