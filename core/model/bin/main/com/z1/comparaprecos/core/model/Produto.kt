package com.z1.comparaprecos.core.model

import java.math.BigDecimal
import java.math.RoundingMode

data class Produto(
    val id: Long,
    val idListaCompra: Long,
    val nomeProduto: String,
    val quantidade: String,
    val precoUnitario: BigDecimal,
    val isMedidaPeso: Boolean,
) {
    fun valorProduto(): BigDecimal =
        precoUnitario.multiply(BigDecimal(quantidade))
            .setScale(3, RoundingMode.HALF_UP)
            .setScale(2, RoundingMode.FLOOR)
    fun compararPreco(valorProdutoComparado: BigDecimal): BigDecimal {
        val diferenca = precoUnitario.minus(valorProdutoComparado)
        if (diferenca.toDouble() == 0.0) return BigDecimal("0.00")
        val porcentagem = diferenca.divide(valorProdutoComparado, 10, RoundingMode.HALF_UP)
            .multiply(BigDecimal(100))
        return porcentagem.setScale(2, RoundingMode.HALF_UP)
    }
}
