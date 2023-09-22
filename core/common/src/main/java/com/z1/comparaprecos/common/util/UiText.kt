package com.z1.comparaprecos.common.util

import android.content.Context

sealed class UiText {
    data class StringResource(val resId: Int): UiText()
    data class StringMessage(val message: String): UiText()

    fun asResId() = when(this) {
        is StringResource -> resId
        else -> 0
    }
    fun asString(context: Context) = when(this) {
        is StringResource -> context.getString(resId)
        is StringMessage -> message
    }
}
