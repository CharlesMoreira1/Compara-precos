package com.z1.comparaprecos.feature.listacompra.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.getBoundsInRoot
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.z1.comparaprecos.common.ui.theme.ComparaPrecosTheme
import com.z1.comparaprecos.feature.listacompra.domain.ListaCompraUseCase
import com.z1.comparaprecos.feature.listacompra.presentation.viewmodel.ListaCompraViewModel
import com.z1.comparaprecos.feature.listacompra.presentation.viewmodel.OnEvent
import com.z1.comparaprecos.testing.data.listaCompraWithProductTestData
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit


class ListaCompraContainerTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val usecase: ListaCompraUseCase = mockk(relaxed = true)
    private lateinit var viewModel: ListaCompraViewModel
    private val goToListaProduto: (Long, Boolean) -> Unit = mockk(relaxed = true)

    private fun init() {
        viewModel = ListaCompraViewModel(usecase)
        composeTestRule.setContent {
            ComparaPrecosTheme {
                ListaCompraContainer(
                    viewModel = viewModel,
                    goToListaProduto = goToListaProduto
                )
            }
        }
    }

    @Test
    fun shouldShowProgressDialogWhenStartingApplication() {
        //Given - Dado
        init()

        //Then - Entao
        composeTestRule.onNodeWithText("Carregando suas listas…").assertIsDisplayed()
    }
}