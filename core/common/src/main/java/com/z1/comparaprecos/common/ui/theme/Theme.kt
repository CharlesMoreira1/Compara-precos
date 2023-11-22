package com.z1.comparaprecos.common.ui.theme

import android.app.Activity
import android.os.Build
import android.provider.CalendarContract.Colors
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.z1.comparaprecos.common.util.ThemeOptions
import java.util.Collections.copy

private val DarkColorScheme = darkColorScheme(
    primary = MediumSlateBlue,
    onPrimary = WhiteSmoke,
    secondary = Glacier,
    onSecondary = WhiteSmoke,
    tertiary = MacaroniAndCheese,
    error = PastelRed,
    background = Peacoat,
    onBackground = WhiteSmoke,
    surface = YankeesBlue,
    onSurface = WhiteSmoke
)

private val LightColorScheme = lightColorScheme(
    primary = Glaucous,
    onPrimary = White,
    secondary = AztecGold,
    onSecondary = White,
    tertiary = CinnamonSatin,
    onTertiary = White,
    error = CoralRed,
    background = AliceBlue,
    surface = White,
    onSurface = Gunmetal
)

private val DoceRubiColorScheme = lightColorScheme(
    primary = AmaranthPurple,
    onPrimary = White,
    secondary = JungleGreen,
    onSecondary = White,
    tertiary = CobaltBlue,
    onTertiary = White,
    error = CoralRed,
    background = Isabelline,
    surface = White,
    onSurface = Gunmetal
)

private val VeraoLaranjaColorScheme = lightColorScheme(
    primary = Tangerine,
    onPrimary = White,
    secondary = BlueCrayola,
    onSecondary = White,
    tertiary = PurpleWine,
    onTertiary = White,
    error = CoralRed,
    background = Linen,
    surface = White,
    onSurface = Gunmetal
)

private val RosaSuaveColorScheme = lightColorScheme(
    primary = DarkMagenta,
    onPrimary = White,
    secondary = IslamicGreen,
    onSecondary = White,
    tertiary = USAirForceAcademyBlue,
    onTertiary = White,
    error = CoralRed,
    background = LavenderBlush,
    surface = White,
    onSurface = Gunmetal
)

private val UvaRoxaColorScheme = lightColorScheme(
    primary = DarkBlue,
    onPrimary = White,
    secondary = OliveDrab,
    onSecondary = White,
    tertiary = OldMossGreen,
    onTertiary = White,
    error = CoralRed,
    background = LavenderMist,
    surface = White,
    onSurface = Gunmetal
)

private val VerdeEucaliptoColorScheme = lightColorScheme(
    primary = GreenNcs,
    onPrimary = White,
    secondary = JazzberryJam,
    onSecondary = White,
    tertiary = MediumPersianBlue,
    onTertiary = White,
    error = CoralRed,
    background = MintCream,
    surface = White,
    onSurface = Gunmetal
)

private val OperaCafeColorScheme = lightColorScheme(
    primary = Black,
    onPrimary = White,
    secondary = Black,
    onSecondary = White,
    tertiary = CoolMint,
    onTertiary = Gunmetal,
    error = CoralRed,
    background = EggShell,
    surface = White,
    onSurface = Gunmetal
)

@Composable
fun ComparaPrecosTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    appTheme: ThemeOptions = ThemeOptions.DEFAULT,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && supportsDynamicTheming() -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        appTheme == ThemeOptions.DEFAULT -> LightColorScheme
        appTheme == ThemeOptions.VERDE_EUCALIPTO -> VerdeEucaliptoColorScheme
        appTheme == ThemeOptions.UVA_ROXA -> UvaRoxaColorScheme
        appTheme == ThemeOptions.ROSA_SUAVE -> RosaSuaveColorScheme
        appTheme == ThemeOptions.VERAO_LARANJA -> VeraoLaranjaColorScheme
        appTheme == ThemeOptions.DOCE_RUBI -> DoceRubiColorScheme
        appTheme == ThemeOptions.OPERA_CAFE -> OperaCafeColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme.switch(),
        typography = Typography,
        content = content
    )
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun supportsDynamicTheming() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

@Composable
fun animatedColor(targetValue: Color) =
    animateColorAsState(
        targetValue = targetValue,
        animationSpec = tween(durationMillis = 500, easing = LinearEasing),
        label = "animate color"
    ).value

@Composable
fun ColorScheme.switch() = copy(
    primary = animatedColor(primary),
    onPrimary = animatedColor(onPrimary),
    secondary = animatedColor(secondary),
    onSecondary = animatedColor(onSecondary),
    tertiary = animatedColor(tertiary),
    onTertiary = animatedColor(onTertiary),
    error = animatedColor(error),
    background = animatedColor(background),
    surface = animatedColor(surface),
    onSurface = animatedColor(onSurface)
)