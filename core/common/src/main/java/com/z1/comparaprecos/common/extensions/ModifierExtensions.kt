package com.z1.comparaprecos.common.extensions

import androidx.compose.ui.Modifier

fun Modifier.thenIf(condition: Boolean, modifier: Modifier.() -> Modifier): Modifier =
    if (condition) then(modifier(Modifier))
    else this

fun Modifier.thenIf2(condition: Boolean, vararg modifier: Modifier.() -> Modifier): Modifier =
    if (condition) {
        var all: Modifier = Modifier
        modifier.forEach {
            all = all.then(it(Modifier))
        }
        then(all)

    } else this