package com.z1.comparaprecos.feature.listaproduto.presentation.screen

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.z1.comparaprecos.common.ui.theme.ComparaPrecosTheme
import com.z1.comparaprecos.core.common.R
import com.z1.comparaprecos.feature.listaproduto.presentation.state.UiEvent
import com.z1.comparaprecos.feature.listaproduto.presentation.state.UiState
import com.z1.comparaprecos.testing.data.listaCompraOptionsDataTest
import com.z1.comparaprecos.testing.data.listaCompraWithProductTestData
import com.z1.comparaprecos.testing.data.listaProdutoDataTest
import com.z1.comparaprecos.testing.data.listaProdutoDataTest2
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class ListaProdutoComparadaScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var fakeUiState: UiState

    private fun init(uiEvent: UiEvent = UiEvent.Default) {
        composeTestRule.setContent {
            ComparaPrecosTheme {
                ListaProdutoComparadaScreen(
                    uiState = fakeUiState,
                    uiEvent = uiEvent,
                    onEvent = {}
                )
            }
        }
    }

    @Test
    fun shouldShowActualListOfProdutoWhenUserEnterInScreen() {
        //Given - Dado
        fakeUiState = UiState(
            listaCompra = listaCompraWithProductTestData[0].detalhes
        )

        //When - Quando
        init()

        //Then - Entao
        composeTestRule.onNodeWithText("ListaCompra 0").assertIsDisplayed()
    }

    @Test
    fun shouldShowMessageToTellUserSelectAListaCompraToCompararWhenUserGoToSecondTab() {
        //Given - Dado
        val message = composeTestRule.activity.getString(
            R.string.label_desc_adicionar_lista_to_comparar
        )
        val listaComparadaTextButton = composeTestRule.activity.getString(
            R.string.label_lista_comparada
        )
        fakeUiState = UiState(
            listaCompra = listaCompraWithProductTestData[0].detalhes
        )

        //When - Quando
        init()
        composeTestRule.onNodeWithText(listaComparadaTextButton).performClick()

        //Then - Entao
        composeTestRule.onNodeWithText(message).assertIsDisplayed()
    }

    @Test
    fun shouldShowDialogWithListaCompraOptionsWhenUserClickAddButtonOnSecondTab() {
        //Given - Dado
        val dialogTitle = composeTestRule.activity.getString(
            R.string.label_selecione_lista_to_comparar
        )
        val listaComparadaTextButton = composeTestRule.activity.getString(
            R.string.label_lista_comparada
        )

        fakeUiState = UiState(
            listaCompra = listaCompraWithProductTestData[0].detalhes,
            listaCompraOptions = listaCompraOptionsDataTest
        )

        //When - Quando
        init(uiEvent = UiEvent.ShowAlertDialog)
        composeTestRule.onNodeWithText(listaComparadaTextButton).performClick()

        //Then - Entao
        composeTestRule.onAllNodesWithText(dialogTitle).onLast().assertIsDisplayed()
    }

    @Test
    fun shouldShowMessageToTellUserAddAListaCompraToCompararBeforeToSeeResumeWhenUserGoToThirdTab() {
        //Given - Dado
        val message = composeTestRule.activity.getString(
            R.string.label_desc_necessario_duas_listas_to_comparar
        )
        val resumoTextButton = composeTestRule.activity.getString(
            R.string.label_resumo
        )
        fakeUiState = UiState(
            listaCompra = listaCompraWithProductTestData[0].detalhes
        )

        //When - Quando
        init()
        composeTestRule.onNodeWithText(resumoTextButton).performClick()

        //Then - Entao
        composeTestRule.onNodeWithText(message).assertIsDisplayed()
    }

    @Test
    fun shouldShowResumeOfComparisonAfterUserAddAListaCompraToCompararWhenUserGoToThirdTab() {
        //Given - Dado
        val listaCompraAtual = listaCompraWithProductTestData[0].copy(produtos = listaProdutoDataTest)
        val listaCompraComparada = listaCompraWithProductTestData[1].copy(produtos = listaProdutoDataTest2)
        val resumoTextButton = composeTestRule.activity.getString(
            R.string.label_resumo
        )
        fakeUiState = UiState(
            listaCompra = listaCompraAtual.detalhes,
            listaProduto = listaCompraAtual.produtos,
            listaCompraComparada = listaCompraComparada
        )

        //When - Quando
        init()
        composeTestRule.onNodeWithText(resumoTextButton).performClick()

        //Then - Entao
        composeTestRule.onNodeWithText(listaCompraAtual.detalhes.titulo).assertIsDisplayed()
        composeTestRule.onNodeWithText(listaCompraComparada.detalhes.titulo).assertIsDisplayed()
    }
}