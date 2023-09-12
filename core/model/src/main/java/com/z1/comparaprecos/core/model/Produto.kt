package com.z1.comparaprecos.core.model

import java.math.BigDecimal

data class Produto(
    val id: Long,
    val idListaCompra: Long,
    val nomeProduto: String,
    val quantidade: Double,
    val precoUnitario: BigDecimal,
) {
    fun precoProduto() = precoUnitario.multiply(BigDecimal.valueOf(quantidade))
}
