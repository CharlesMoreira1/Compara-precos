package com.z1.comparaprecos.core.model

import java.math.BigDecimal
import java.math.RoundingMode

data class Produto(
    val id: Long,
    val idListaCompra: Long,
    val nomeProduto: String,
    val quantidade: String,
    val precoUnitario: BigDecimal,
    val medida: String,
) {
    fun precoProdutoTotal(): BigDecimal =
        precoUnitario.multiply(
            BigDecimal(quantidade)
                .setScale(3, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.FLOOR)
        )

}
