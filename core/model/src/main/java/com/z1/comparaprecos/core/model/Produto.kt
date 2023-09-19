package com.z1.comparaprecos.core.model

import java.math.BigDecimal

data class Produto(
    val id: Long,
    val idListaCompra: Long,
    val nomeProduto: String,
    val quantidade: Double,
    val precoUnitario: BigDecimal,
) {
    fun precoProdutoTotal(): BigDecimal = precoUnitario.multiply(BigDecimal.valueOf(quantidade))

}
