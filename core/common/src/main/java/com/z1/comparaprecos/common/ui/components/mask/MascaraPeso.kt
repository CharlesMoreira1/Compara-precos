package com.z1.comparaprecos.common.ui.components.mask

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.DecimalFormat
import java.util.Currency
import java.util.Locale

class MascaraPeso(
    private val fixedCursorAtTheEnd: Boolean = true,
    private val numberOfDecimals: Int = 3
) : VisualTransformation {
    private val symbols = DecimalFormat().decimalFormatSymbols
    override fun filter(text: AnnotatedString): TransformedText {
        val thousandsSeparator = symbols.groupingSeparator
        val decimalSeparator = symbols.decimalSeparator
        val zero = symbols.zeroDigit

        val inputText = text.text

        val intPart = inputText
            .dropLast(numberOfDecimals)
            .reversed()
            .chunked(3)
            .joinToString(thousandsSeparator.toString())
            .reversed()
            .ifEmpty {
                zero.toString()
            }

        val fractionPart = inputText.takeLast(numberOfDecimals).let {
            if (it.length != numberOfDecimals) {
                List(numberOfDecimals - it.length) {
                    zero
                }.joinToString("") + it
            } else {
                it
            }
        }

        val formattedNumber = intPart + decimalSeparator + fractionPart + " kg"

        val newText = AnnotatedString(
            text = formattedNumber,
            spanStyles = text.spanStyles,
            paragraphStyles = text.paragraphStyles
        )

        val offsetMapping = if (fixedCursorAtTheEnd) {
            FixedCursorOffsetMapping(
                contentLength = inputText.length,
                formattedContentLength = formattedNumber.length
            )
        } else {
            MovableCursorOffsetMapping(
                unmaskedText = text.toString(),
                maskedText = newText.toString(),
                decimalDigits = numberOfDecimals
            )
        }
        return TransformedText(newText, offsetMapping)
    }
}