package com.z1.comparaprecos.feature.listaproduto.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.z1.comparaprecos.common.ui.theme.ComparaPrecosTheme
import com.z1.comparaprecos.core.model.Produto
import com.z1.comparaprecos.testing.data.listaProdutoDataTest
import org.junit.Rule
import org.junit.Test

class FormularioProdutoTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private fun init(produto: Produto?) {
        composeTestRule.setContent {
            ComparaPrecosTheme {
                FormularioProduto(
                    produtoSelecionado = produto,
                    idListaCompra = 0,
                    onAdicionarProdutoClick = {},
                    onDeletarProdutoClick = {},
                    onCancelarEdicaoProduto = {}
                )
            }
        }
    }

    @Test
    fun shouldShowAddProdutoNameWhenUserAddingProduto() {
        //When - Quando
        init(null)

        //Then - Entao
        composeTestRule.onNodeWithText("Adicionar produto").assertIsDisplayed()
    }

    @Test
    fun shouldShowEditProdutoNameWhenUserEditingProduto() {
        //Given - Dado
        val produto = listaProdutoDataTest[0]

        //When - Quando
        init(produto)

        //Then - Entao
        composeTestRule.onNodeWithText("Editar produto").assertIsDisplayed()
    }

    @Test
    fun shouldShowPesoNameWhenUserClickInIsPesoCheckBox() {
        //Given - Dado
        init(null)

        //When - Quando
        composeTestRule.onNodeWithText("Peso").performClick()

        //Then - Entao
        composeTestRule.onNodeWithText("0,000 kg").assertIsDisplayed()
    }

    @Test
    fun shouldShowRedBorderWhenUserClickAddProdutoWithoutName() {
        //Given - Dado
        init(null)

        //When - Quando
        composeTestRule.onNodeWithText("Produto").performClick()
        composeTestRule.onNodeWithText("Adicionar produto").performClick()

        //Then - Entao
        composeTestRule.onNodeWithText("Esse produto é …").assertIsDisplayed()
    }

    @Test
    fun shouldShowNewQuantityWhenUserClickAddQuantity() {
        //Given - Dado
        init(null)

        //When - Quando
        composeTestRule.onNodeWithContentDescription("Adicionar quantidade").performClick()

        //Then - Entao
        composeTestRule.onNodeWithText("2").assertIsDisplayed()
    }

    @Test
    fun shouldShowNewQuantityWhenUserClickRemoveQuantity() {
        //Given - Dado
        init(null)

        //When - Quando
        composeTestRule.onNodeWithContentDescription("Adicionar quantidade").performClick()
        composeTestRule.onNodeWithContentDescription("Remover quantidade").performClick()

        //Then - Entao
        composeTestRule.onNodeWithText("1").assertIsDisplayed()
    }
}