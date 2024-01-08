package com.z1.comparaprecos.common.ui.components.mask.offsetmapping

import androidx.compose.ui.text.input.OffsetMapping

class MovableCursorOffsetMapping(
    private val unmaskedText: String,
    private val maskedText: String,
    private val decimalDigits: Int
) : OffsetMapping {
    override fun originalToTransformed(offset: Int): Int =
        when {
            unmaskedText.length <= decimalDigits -> {
                maskedText.length - (unmaskedText.length - offset)
            }

            else -> {
                offset + offsetMaskCount(offset, maskedText)
            }
        }

    override fun transformedToOriginal(offset: Int): Int =
        when {
            unmaskedText.length <= decimalDigits -> {
                Integer.max(unmaskedText.length - (maskedText.length - offset), 0)
            }

            else -> {
                offset - maskedText.take(offset).count { !it.isDigit() }
            }
        }

    private fun offsetMaskCount(offset: Int, maskedText: String): Int {
        var maskOffsetCount = 0
        var dataCount = 0
        for (maskChar in maskedText) {
            if (!maskChar.isDigit()) {
                maskOffsetCount++
            } else if (++dataCount > offset) {
                break
            }
        }
        return maskOffsetCount
    }
}