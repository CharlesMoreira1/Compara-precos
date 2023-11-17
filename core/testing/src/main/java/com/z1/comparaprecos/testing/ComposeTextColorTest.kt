package com.z1.comparaprecos.testing


import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import androidx.compose.ui.text.TextLayoutResult

fun SemanticsNodeInteraction.assertTextColor(color: Color)
: SemanticsNodeInteraction = assert(isOfColor(color))

private fun isOfColor(color: Color): SemanticsMatcher = SemanticsMatcher(
    "${SemanticsProperties.Text.name} is color of '$color'"
) {
    val textLayoutResult = mutableListOf<TextLayoutResult>()
    it.config.getOrNull(SemanticsActions.GetTextLayoutResult)
        ?.action
        ?.invoke(textLayoutResult)
    return@SemanticsMatcher if (textLayoutResult.isEmpty()) {
        false
    } else {
        textLayoutResult.first().layoutInput.style.color == color
    }
}