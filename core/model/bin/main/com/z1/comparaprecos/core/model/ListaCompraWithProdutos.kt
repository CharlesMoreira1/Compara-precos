package com.z1.comparaprecos.core.model

import java.math.BigDecimal
import java.math.RoundingMode

data class ListaCompraWithProdutos(
    val detalhes: ListaCompra,
    val produtos: List<Produto>
) {
    fun valorTotal(): BigDecimal = produtos.sumOf { it.valorProduto() }
        .setScale(3, RoundingMode.HALF_UP)
        .setScale(2, RoundingMode.FLOOR)
}
