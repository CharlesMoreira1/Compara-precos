package com.z1.comparaprecos.common.util

import com.z1.comparaprecos.core.common.R

enum class ThemeOptions(val idTema: Long) {
    DEFAULT(0),
    VERDE_EUCALIPTO(1),
    UVA_ROXA(2),
    ROSA_SUAVE(3),
    VERAO_LARANJA(4),
    DOCE_RUBI(5),
    OPERA_CAFE(6),
}

enum class DarkThemeMode(val darkThemeModeId: Long) {
    LIGHT(0L),
    DARK(1L),
    FOLLOW_SYSTEM(2L),
}

fun findThemeById(idTema: Long): ThemeOptions {
    for (theme in ThemeOptions.values()) {
        if (theme.idTema == idTema) {
            return theme
        }
    }
    return ThemeOptions.DEFAULT
}

val listaTemas = listOf(
    R.string.label_default to ThemeOptions.DEFAULT.idTema,
    R.string.label_verde_eucalipto to ThemeOptions.VERDE_EUCALIPTO.idTema,
    R.string.label_uva_roxa to ThemeOptions.UVA_ROXA.idTema,
    R.string.label_rosa_suave to ThemeOptions.ROSA_SUAVE.idTema,
    R.string.label_verao_laranja to ThemeOptions.VERAO_LARANJA.idTema,
    R.string.label_doce_rubi to ThemeOptions.DOCE_RUBI.idTema,
    R.string.label_opera_cafe to ThemeOptions.OPERA_CAFE.idTema
)