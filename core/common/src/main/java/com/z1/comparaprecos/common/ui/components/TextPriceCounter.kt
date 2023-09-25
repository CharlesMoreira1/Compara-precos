package com.z1.comparaprecos.common.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.z1.comparaprecos.common.extensions.toRoundDecimalsPlaces
import java.math.BigDecimal

data class PriceDigit(val singleDigit: Char, val fullNumber: Int, val place: Int) {
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is PriceDigit -> singleDigit == other.singleDigit
            else -> super.equals(other)
        }
    }

    override fun hashCode(): Int {
        return singleDigit.hashCode()
    }
}

private operator fun PriceDigit.compareTo(other: PriceDigit): Int {
    return fullNumber.compareTo(other.fullNumber)
}

@Composable
fun CustomTextPriceCounter(
    modifier: Modifier = Modifier,
    textStyle: TextStyle,
    price: BigDecimal
) {
    val counter = price.multiply(BigDecimal(100))
        .toRoundDecimalsPlaces(3, 2)
        .toInt()
    val counterText = counter.toString().padStart(3, '0')

    val durations = when (counterText.length) {
        4 -> intArrayOf(1600, 1500, 1400, 1300, 1200, 1100, 1000, 900, 800, 700, 600, 500)
        5 -> intArrayOf(1600, 1500, 1400, 1300, 1200, 1100, 1000, 900, 800, 700, 600)
        6 -> intArrayOf(1600, 1500, 1400, 1300, 1200, 1100, 1000, 900, 800, 700)
        7 -> intArrayOf(1600, 1500, 1400, 1300, 1200, 1100, 1000, 900, 800)
        8 -> intArrayOf(1600, 1500, 1400, 1300, 1200, 1100, 1000, 900)
        9 -> intArrayOf(1600, 1500, 1400, 1300, 1200, 1100, 1000)
        10 -> intArrayOf(1600, 1500, 1400, 1300, 1200, 1100)
        11 -> intArrayOf(1600, 1500, 1400, 1300, 1200)
        else -> IntArray(12) { 0 }
    }

    val separatorPointIndices = when (counterText.length) {
        3 -> intArrayOf(1, -1, -1) //0,00
        4 -> intArrayOf(2, -1, -1) //00,00
        5 -> intArrayOf(3, -1, -1) //000,00
        6 -> intArrayOf(4, 1, -1) //0.000,00
        7 -> intArrayOf(5, 2, -1) //00.000,00
        8 -> intArrayOf(6, 3, -1) //000.000,00
        9 -> intArrayOf(7, 4, 1) //0.000.000,00
        10 -> intArrayOf(8, 5, 2) //00.000.000,00
        11 -> intArrayOf(9, 6, 3) //000.000.000,00
        else -> intArrayOf()
    }

    val decimalPointIndices = when (counterText.length) {
        3 -> intArrayOf(1, -1, -1) //0,00
        4 -> intArrayOf(2, -1, -1) //00,00
        5 -> intArrayOf(3, -1, -1) //000,00

        else -> intArrayOf()
    }

    val thousandsSeparatorIndices = when(counterText.length) {
        6 -> intArrayOf(1, 4, -1) //0.000,00
        7 -> intArrayOf(2, 5, -1) //00.000,00
        8 -> intArrayOf(3, 6, -1) //000.000,00
        else -> intArrayOf()
    }

    val millionsSeparatorIndices = when (counterText.length) {
        9 -> intArrayOf(1, 4, 7) //0.000.000,00
        10 -> intArrayOf(2, 5, 8) //00.000.000,00
        11 -> intArrayOf(3, 6, 9) //000.000.000,00
        else -> intArrayOf()
    }

    val isDecimalSeparator: (Int) -> Boolean = { index -> separatorPointIndices[0] == index }
    val isThousandsSeparator: (Int) -> Boolean = { index -> separatorPointIndices[1] == index }
    val isMillionsSeparator: (Int) -> Boolean = { index -> separatorPointIndices[2] == index }

    counterText.reversed()
        .mapIndexed { index, char -> PriceDigit(char, counter, index) }
        .reversed()
        .forEachIndexed { index, digit ->

            val duration = durations.getOrElse(index) { 0 }

            val isDecimal = isDecimalSeparator(index)
            val isThousands = isThousandsSeparator(index)
            val isMillions = isMillionsSeparator(index)

            when {
                isDecimal -> {
                    AdicionarSeparador(text = ",", textStyle = textStyle)
                }
                isThousands -> {
                    AdicionarSeparador(text = ".", textStyle = textStyle)
                }
                isMillions -> {
                    AdicionarSeparador(text = ".", textStyle = textStyle)
                }
            }

//            if (isDecimal) {
//                AdicionarSeparador(text = if (isThousands || isMillions) "." else ",", textStyle = textStyle)
//            }

            AnimatedContent(
                targetState = digit,
                transitionSpec = {
                    if (targetState > initialState) {
                        // Enter Transition            Exit Transition
                        slideInVertically(tween(duration, 0, FastOutLinearInEasing)) { -it } togetherWith
                                (slideOutVertically(tween(duration, 0, FastOutSlowInEasing)) { it } )
                    } else {
                        // Exit Transition
                        slideInVertically(tween(duration, 0, FastOutSlowInEasing)) { it } togetherWith
                                (slideOutVertically(tween(duration, 0, FastOutSlowInEasing)) { -it } )
                    }
                },
                label = "text animation"
            ) { updatedCount ->

                Text(
                    modifier = modifier,
                    text = "${updatedCount.singleDigit}",
                    textAlign = TextAlign.Center,
                    style = textStyle
                )

            }
        }

}

@Composable
fun AdicionarSeparador(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle
) {
    Text(
        modifier = modifier,
        text = text,
        textAlign = TextAlign.Center,
        style = textStyle
    )
}