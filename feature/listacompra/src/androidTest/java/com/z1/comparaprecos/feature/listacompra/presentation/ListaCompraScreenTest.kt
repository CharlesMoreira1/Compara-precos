package com.z1.comparaprecos.feature.listacompra.presentation

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.z1.comparaprecos.common.ui.theme.ComparaPrecosTheme
import com.z1.comparaprecos.core.common.R
import com.z1.comparaprecos.feature.listacompra.presentation.viewmodel.OnEvent
import com.z1.comparaprecos.testing.data.listaCompraWithProductTestData
import io.mockk.mockk
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit

class ListaCompraScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var fakeUiState: UiState
    private val goToListaProduto: (Long, Boolean) -> Unit = mockk(relaxed = true)

    private fun init(uiEvent: UiEvent = UiEvent.Default) {
        composeTestRule.setContent {
            ComparaPrecosTheme {
                ListaCompraScreen(
                    uiState = fakeUiState,
                    uiEvent = uiEvent,
                    onEvent = { },
                    goToListaProduto = goToListaProduto
                )
            }
        }
    }

    @Test
    fun shouldShowMessageOnScreenWhenUserNotCreatedAnyListaCompra() {
        //Given - Dado
        fakeUiState = UiState()

        //When - Quando
        init()

        //Then - Entao
        composeTestRule.onNodeWithText("Você não criou nenhuma lista de compra. Vamos começar agora? Clique no botão +.")
    }

    @Test
    fun shouldShowListOfListaCompraOnScreenWhenUserHasListaCompraCreated() {
        //Given - Dado
        fakeUiState = UiState(
            listaCompra = listaCompraWithProductTestData
        )

        //When - Quando
        init()

        //Then - Entao
        composeTestRule.onNodeWithText("ListaCompra 0")
    }

    @Test
    fun shouldShowBottomSheetScafoldWhenUserPressCreateListaCompraButton() {
        //Given - Dado
        fakeUiState = UiState()

        //When - Quando
        init(UiEvent.ShowBottomSheet)

        //Then - Entao
        composeTestRule.onNodeWithText("Nova lista").assertIsDisplayed()
        composeTestRule.onNodeWithText("Criar lista de compra").assertIsDisplayed()
    }

    @Test
    fun shouldShowBottomSheetOptionsWhenUserSelectListaCompra() {
        //Given - Dado
        fakeUiState = UiState(
            listaCompra = listaCompraWithProductTestData,
            listaCompraSelecionada = listaCompraWithProductTestData[0]
        )

        //When - Quando
        init(UiEvent.ShowBottomSheetOptions)

        //Then - Entao
        composeTestRule.onNodeWithText("Deletar lista").assertIsDisplayed()
    }

    @Test
    fun shouldShowBottomSheetScafoldWhenUserPressRenameListaCompraButton() {
        //Given - Dado
        fakeUiState = UiState(
            tituloBottomSheet = R.string.label_renomear_lista,
            descricaoBottomSheet = R.string.label_desc_renomear_lista_compra,
            buttonBottomSheet = R.string.label_renomear_lista_compra,
            isRenomearListaCompra = true,
            tituloListaCompra = listaCompraWithProductTestData[0].detalhes.titulo
        )

        //When - Quando
        init(UiEvent.ShowBottomSheet)

        //Then - Entao
        composeTestRule.onNodeWithText("Renomear lista").assertIsDisplayed()
        composeTestRule.onNodeWithText("Renomear lista de compra").assertIsDisplayed()
    }

    @Test
    fun shouldShowBottomSheetScafoldWhenUserPressDuplicateListaCompraButton() {
        //Given - Dado
        fakeUiState = UiState(
            tituloBottomSheet = R.string.label_duplicar_lista,
            descricaoBottomSheet = R.string.label_desc_duplicar_lista,
            buttonBottomSheet = R.string.label_duplicar_lista_compra,
            isDuplicarListaCompra = true,
            tituloListaCompra = ""
        )

        //When - Quando
        init(UiEvent.ShowBottomSheet)

        //Then - Entao
        composeTestRule.onNodeWithText("Duplicar lista").assertIsDisplayed()
        composeTestRule.onNodeWithText("Duplicar lista de compra").assertIsDisplayed()
    }

    @Test
    fun shouldRemoveListaCompraFromListWhenUserDeleteListaCompra() {
        val lista = listaCompraWithProductTestData.toMutableList()
        //Given - Dado
        fakeUiState = UiState(
            listaCompra = lista,
            listaCompraSelecionada = lista[0]
        )

        //When - Quando
        lista.removeAt(0)
        init(UiEvent.Deleted(R.string.label_desc_lista_compra_excluida))

        //Then - Entao
        composeTestRule.onNodeWithText("ListaCompra 0").assertDoesNotExist()
    }

    @Test
    fun shouldShowWarningMessageWhenUserTryCreateListaCompraWithoutTitle() {
        //Given - Dado
        fakeUiState = UiState(
            isTituloVazio = true
        )

        //When - Quando
        init(UiEvent.ShowBottomSheet)

        //Then - Entao
        composeTestRule.onNodeWithText("Campo obrigatório!").assertIsDisplayed()
    }

    @Test
    fun shouldShowBottomSheetOptionsWithoutOpenListComparingOptionWhenUserhasOnlyOneListaCompra() {
        //Given - Dado
        fakeUiState = UiState(
            listaCompra = listOf(listaCompraWithProductTestData[0]),
            listaCompraSelecionada = listaCompraWithProductTestData[0]
        )

        //When - Quando
        init(UiEvent.ShowBottomSheetOptions)

        //Then - Entao
        composeTestRule.onNodeWithText("Abir lista comparando").assertDoesNotExist()
    }

    @Test
    fun shouldHideBottomSheetScafoldWhenUserPressCloseButton() {
        //Given - Dado
        fakeUiState = UiState()

        //When - Quando
        init(UiEvent.HideBottomSheet)

        //Then - Entao
        composeTestRule.onNodeWithText("Nova lista").assertIsNotDisplayed()
    }
}