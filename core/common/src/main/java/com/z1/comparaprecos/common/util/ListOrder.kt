package com.z1.comparaprecos.common.util

import androidx.annotation.StringRes
import com.z1.comparaprecos.core.common.R

enum class ListOrder(val id: Long, @StringRes val title: Int) {
    A_Z(0L, R.string.label_de_a_z),
    Z_A(1L, R.string.label_de_z_a),
    ADICIONADO_PRIMEIRO(2L, R.string.label_adicionado_primeiro),
    ADICIONADO_ULTIMO(3L, R.string.label_adicionado_ultimo)
}

val ordenacaoOptions = listOf(
    ListOrder.A_Z.title to ListOrder.A_Z.id,
    ListOrder.Z_A.title to ListOrder.Z_A.id,
    ListOrder.ADICIONADO_PRIMEIRO.title to ListOrder.ADICIONADO_PRIMEIRO.id,
    ListOrder.ADICIONADO_ULTIMO.title to ListOrder.ADICIONADO_ULTIMO.id,
)

fun findListOrderById(ordenacaoId: Long): ListOrder {
    for (listOrder in ListOrder.values()) {
        if (listOrder.id == ordenacaoId) {
            return listOrder
        }
    }
    return ListOrder.ADICIONADO_PRIMEIRO
}