package com.z1.comparaprecos.feature.listaproduto.presentation

import com.z1.comparaprecos.core.model.ListaCompra
import com.z1.comparaprecos.core.model.ListaCompraWithProdutos
import com.z1.comparaprecos.core.model.Produto
import java.math.BigDecimal

data class UiState(
    val isListaProdutoCarregada: Boolean = false,
    val produtoSelecionado: Produto? = null,
    val produtoJaExiste: Produto? = null,
    val listaCompra: ListaCompra = ListaCompra(-1, "", 0L),
    val allListaCompra: List<Pair<String, Long>> = emptyList(),
    val listaProduto: List<Produto> = emptyList(),
    val listaCompraComparada: ListaCompraWithProdutos = ListaCompraWithProdutos(listaCompra, emptyList())
)