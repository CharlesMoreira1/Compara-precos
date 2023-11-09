package com.z1.comparaprecos.feature.listaproduto.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.z1.comparaprecos.common.ui.theme.ComparaPrecosTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal
import java.util.concurrent.TimeUnit

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