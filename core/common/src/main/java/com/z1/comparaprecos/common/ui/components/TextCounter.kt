package com.z1.comparaprecos.common.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Digit(val singleDigit: Char, val fullNumber: Int, val place: Int) {
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Digit -> singleDigit == other.singleDigit
            else -> super.equals(other)
        }
    }

    override fun hashCode(): Int {
        return singleDigit.hashCode()
    }
}

private operator fun Digit.compareTo(other: Digit): Int {
    return fullNumber.compareTo(other.fullNumber)
}

@Composable
fun CustomTextCounter(
    modifier: Modifier = Modifier,
    counter: Int
) {

    counter.toString().reversed()
        .mapIndexed { index, char -> Digit(char, counter, index) }
        .reversed()
        .forEach { digit ->
            AnimatedContent(
                targetState = digit,
                transitionSpec = {
                    if (targetState > initialState) {
                        // Enter Transition            Exit Transition
                        slideInVertically { -it } togetherWith (slideOutVertically { it } + fadeOut())
                    } else {
                        // Exit Transition
                        slideInVertically { it } togetherWith (slideOutVertically { -it } + fadeOut())
                    }
                },
                label = "text animation"
            ) { updatedCount ->
                Text(
                    modifier = modifier,
                    text = "${updatedCount.singleDigit}",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

            }
        }


}