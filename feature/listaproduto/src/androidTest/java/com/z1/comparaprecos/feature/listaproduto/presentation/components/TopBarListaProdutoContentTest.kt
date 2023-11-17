package com.z1.comparaprecos.feature.listaproduto.presentation.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.z1.comparaprecos.common.ui.theme.ComparaPrecosTheme
import com.z1.comparaprecos.feature.listaproduto.presentation.components.TituloListaProduto
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal

class TopBarListaProdutoContentTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private fun init() {
        composeTestRule.setContent {
            ComparaPrecosTheme {
                TituloListaProduto(
                    titulo = "Test",
                    valorLista = BigDecimal("109.34")
                )
            }
        }
    }

    @Test
    fun shouldShowNameAndPriceOfListOfProduto() {
        //When - Quando
        init()

        //Then - Entao
        composeTestRule.onNodeWithText("Test").assertIsDisplayed()
        composeTestRule.onNodeWithText("R$ ")
        composeTestRule.onNodeWithText("1")
        composeTestRule.onNodeWithText("0")
        composeTestRule.onNodeWithText("9")
        composeTestRule.onNodeWithText(",")
        composeTestRule.onNodeWithText("3")
        composeTestRule.onNodeWithText("4")
    }
}