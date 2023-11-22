package com.z1.comparaprecos.common.extensions

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toDataLegivel(): String {
    val sdf = SimpleDateFormat("E, dd MMM HH:mm", Locale.getDefault())
    val netDate = Date(this)
    return sdf.format(netDate).replace(".", "")
}

fun BigDecimal.toMoedaLocal(): String {
    return NumberFormat
        .getCurrencyInstance(Locale.getDefault())
        .format(this)
}

fun BigDecimal.toRoundDecimalsPlaces(halfUp: Int, floor: Int): BigDecimal {
    return this.setScale(halfUp, RoundingMode.HALF_UP)
        .setScale(floor, RoundingMode.FLOOR)
}

fun BigDecimal.getPercentageDifference(compare: BigDecimal): String {
    val diferenca = this.minus(compare)
    if (diferenca.toDouble() == 0.0) return "0.00 %"
    val porcentagem = diferenca.divide(compare, 10, RoundingMode.HALF_UP)
        .multiply(BigDecimal(100))
        .setScale(2, RoundingMode.HALF_UP)

    return when {
        porcentagem < BigDecimal.ZERO -> "$porcentagem %"
        porcentagem == BigDecimal("0.00") -> "$porcentagem %"
        else -> "+$porcentagem %"
    }
}

fun BigDecimal.removeZerosFromLeft(): String {
    val formato = DecimalFormat("0.00")
    val valorFormatado = formato.format(this.toDouble())
    val valor = valorFormatado.toString().replace(",", "")
    val array = valor.toCharArray()
    var startIndex = 0
    for (i in array.indices) {
        if (valor[i] != '0') {
            startIndex = i
            break
        }
    }
    return if (valor.toInt() == 0) "" else valor.substring(startIndex)
}

fun String.removeZerosFromLeft(): String {
    val formato = DecimalFormat("0.000")
    val valorFormatado = formato.format(this.toDouble())
    val valor = valorFormatado.toString().replace(",", "")
    val array = valor.toCharArray()
    var startIndex = 0
    for (i in array.indices) {
        if (valor[i] != '0') {
            startIndex = i
            break
        }
    }
    return if (valor.toInt() == 0) "" else valor.substring(startIndex)
}