package com.z1.comparaprecos.feature.listaproduto.presentation.screen

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.z1.comparaprecos.common.ui.components.ETipoSnackbar
import com.z1.comparaprecos.common.ui.theme.ComparaPrecosTheme
import com.z1.comparaprecos.common.util.UiText
import com.z1.comparaprecos.core.common.R
import com.z1.comparaprecos.feature.listaproduto.presentation.screen.ListaProdutoScreen
import com.z1.comparaprecos.feature.listaproduto.presentation.state.UiEvent
import com.z1.comparaprecos.feature.listaproduto.presentation.state.UiState
import com.z1.comparaprecos.testing.data.listaProdutoDataTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ListaProdutoScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var fakeUiState: UiState

    private fun init(uiEvent: UiEvent = UiEvent.Default) {
        composeTestRule.setContent {
            ComparaPrecosTheme {
                ListaProdutoScreen(
                    navigateUp = { },
                    uiState = fakeUiState,
                    uiEvent = uiEvent,
                    onEvent = { }
                )
            }
        }
    }

    @Test
    fun shouldShowWarningMessageWhenUserAddAnExistingProduto() {
        //Given - Dado
        val produto = listaProdutoDataTest[0]
        val warningMessage = composeTestRule.activity.getString(
            R.string.label_desc_produto_existente,
            produto.nomeProduto,
            produto.quantidade
        )
        fakeUiState = UiState(
            listaProduto = listaProdutoDataTest,
            produtoJaExiste = produto
        )

        //When - Quando
        init(
            uiEvent = UiEvent.Error(
                UiText.StringResource(
                    resId = R.string.label_desc_produto_existente,
                    produto.nomeProduto,
                    produto.quantidade
                )
            )
        )

        //Then - Entao
        composeTestRule.onNodeWithText(warningMessage).assertIsDisplayed()
    }

    @Test
    fun shouldShowSnackBarMessageWhenUserEditAnExistingProduto() {
        //Given - Dado
        val produto = listaProdutoDataTest[0]
        val editedProdutoMessage = composeTestRule.activity.getString(R.string.label_produto_editado)
        fakeUiState = UiState(
            listaProduto = listaProdutoDataTest,
            produtoJaExiste = produto
        )

        //When - Quando
        init(
            uiEvent = UiEvent.ShowSnackbar(
                UiText.StringResource(R.string.label_produto_editado),
                ETipoSnackbar.SUCESSO
            )
        )

        //Then - Entao
        composeTestRule.onNodeWithText(editedProdutoMessage).assertIsDisplayed()
    }

}