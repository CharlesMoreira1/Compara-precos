package com.z1.comparaprecos.common.ui.extensions

import java.math.BigDecimal
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