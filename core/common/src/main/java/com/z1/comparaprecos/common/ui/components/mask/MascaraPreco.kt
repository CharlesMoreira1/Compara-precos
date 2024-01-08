package com.z1.comparaprecos.common.ui.components.mask

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.z1.comparaprecos.common.ui.components.mask.offsetmapping.FixedCursorOffsetMapping
import com.z1.comparaprecos.common.ui.components.mask.offsetmapping.MovableCursorOffsetMapping
import java.text.DecimalFormat

class MascaraPreco(
    private val fixedCursorAtTheEnd: Boolean,
    private val numberOfDecimals: Int,
    private val currencySymbol: String
) : VisualTransformation {
    private val symbols = DecimalFormat().decimalFormatSymbols
    override fun filter(text: AnnotatedString): TransformedText {
        val thousandsSeparator = symbols.groupingSeparator
        val decimalSeparator = symbols.decimalSeparator
        val zero = symbols.zeroDigit

        val originalText = text.text

        val thousandPart = originalText
            .dropLast(numberOfDecimals)
            .reversed()
            .chunked(3)
            .joinToString(thousandsSeparator.toString())
            .reversed()
            .ifEmpty {
                zero.toString()
            }

        val decimalPart = originalText.takeLast(numberOfDecimals).let {
            if (it.length != numberOfDecimals) {
                List(numberOfDecimals - it.length) { zero }.joinToString("") + it
            } else {
                it
            }
        }

        val formattedText = "$currencySymbol $thousandPart$decimalSeparator$decimalPart"

        val newText = AnnotatedString(
            text = formattedText,
            spanStyles = text.spanStyles,
            paragraphStyles = text.paragraphStyles
        )

        val offsetMapping = if (fixedCursorAtTheEnd) {
            FixedCursorOffsetMapping(
                contentLength = originalText.length,
                formattedContentLength = formattedText.length
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

@Composable
fun rememberPriceVisualTransformation(
    currencySymbol: String = "",
    numberOfDecimals: Int = 2,
    fixedCursorAtTheEnd: Boolean = true
): VisualTransformation {
    val inspectionMode = LocalInspectionMode.current
    return remember(currencySymbol) {
        if (inspectionMode) {
            VisualTransformation.None
        } else {
            MascaraPreco(
                fixedCursorAtTheEnd = fixedCursorAtTheEnd,
                currencySymbol = currencySymbol,
                numberOfDecimals = numberOfDecimals
            )
        }
    }
}

