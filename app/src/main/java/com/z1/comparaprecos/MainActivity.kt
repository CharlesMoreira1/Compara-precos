package com.z1.comparaprecos

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AnticipateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.z1.comparaprecos.common.ui.theme.ComparaPrecosTheme
import com.z1.comparaprecos.common.util.DarkThemeMode
import com.z1.comparaprecos.common.util.ThemeOptions
import com.z1.comparaprecos.common.util.findThemeById
import com.z1.comparaprecos.core.navigation.NavigationGraph
import com.z1.comparaprecos.core.navigation.navgraph.FeatureNavigationGraph
import com.z1.comparaprecos.viewmodel.MainViewModel
import com.z1.comparaprecos.viewmodel.MainViewModel.MainActivityUiState
import com.z1.comparaprecos.viewmodel.MainViewModel.MainActivityUiState.Loading
import com.z1.comparaprecos.viewmodel.MainViewModel.MainActivityUiState.Success
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var uiState: MainActivityUiState by mutableStateOf(Loading)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.uiState
                    .onEach {
                        uiState = it
                    }
                    .collect()
            }
        }

        splashScreen.setKeepOnScreenCondition {
            when(uiState) {
                Loading -> false
                is Success -> true
            }
        }

        setContent {
            ComparaPrecosTheme(
                appTheme = selectedTheme(uiState),
                dynamicColor = useDynamicColor(uiState),
                darkTheme = useDarkTheme(uiState)
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when(uiState) {
                        is Success -> {
                            ComparaPrecosApp(
                                featureNavigationGraph = (uiState as Success).featureNavigationGraph,
                                onboarded = (uiState as Success).userData.onboarded
                            )
                        }
                        Loading -> Unit
                    }
                }
            }
        }
    }
}

@Composable
fun ComparaPrecosApp(
    modifier: Modifier = Modifier,
    featureNavigationGraph: FeatureNavigationGraph,
    onboarded: Boolean
) {
    NavigationGraph(
        modifier = modifier,
        featureNavigationGraph = featureNavigationGraph,
        onboarded = onboarded

    )
}

@Composable
fun selectedTheme(
    uiState: MainActivityUiState
): ThemeOptions = when (uiState) {
    is Loading -> ThemeOptions.DEFAULT
    is Success -> findThemeById(uiState.userData.themeId)
}

@Composable
fun useDynamicColor(
    uiState: MainActivityUiState
): Boolean = when (uiState) {
    is Loading -> false
    is Success -> uiState.userData.useDynamicColor == 1L
}

@Composable
private fun useDarkTheme(
    uiState: MainActivityUiState,
): Boolean = when (uiState) {
    Loading -> isSystemInDarkTheme()
    is Success -> when (uiState.userData.darkThemeMode) {
        DarkThemeMode.LIGHT.darkThemeModeId -> false
        DarkThemeMode.DARK.darkThemeModeId -> true
        DarkThemeMode.FOLLOW_SYSTEM.darkThemeModeId -> isSystemInDarkTheme()
        else -> false
    }
}