package com.z1.comparaprecos.common.ui.components.mask.offsetmapping

import androidx.compose.ui.text.input.OffsetMapping

class FixedCursorOffsetMapping(
    private val contentLength: Int,
    private val formattedContentLength: Int,
) : OffsetMapping {
    override fun originalToTransformed(offset: Int): Int = formattedContentLength
    override fun transformedToOriginal(offset: Int): Int = contentLength
}