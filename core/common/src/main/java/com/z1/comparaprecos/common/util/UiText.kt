package com.z1.comparaprecos.common.util

import android.content.Context

sealed class UiText {
    data class StringResource(val resId: Int): UiText()

    fun asString(context: Context) = when(this) {
        is StringResource -> context.getString(resId)
    }
}
