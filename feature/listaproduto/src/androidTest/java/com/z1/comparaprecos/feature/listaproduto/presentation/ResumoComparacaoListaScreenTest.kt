package com.z1.comparaprecos.feature.listaproduto.presentation

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import com.z1.comparaprecos.common.ui.theme.ComparaPrecosTheme
import com.z1.comparaprecos.common.ui.theme.CoralRed
import com.z1.comparaprecos.common.ui.theme.MediumSeaGreen
import com.z1.comparaprecos.core.common.R
import com.z1.comparaprecos.testing.assertTextColor
import com.z1.comparaprecos.testing.data.listaProdutoDataTest
import com.z1.comparaprecos.testing.data.listaProdutoDataTest2
import org.junit.Rule
import org.junit.Test

class ResumoComparacaoListaScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val listaProduto = "Lista compra" to listaProdutoDataTest
    private val listaProdutoComparada = "Lista compra comparada" to listaProdutoDataTest2
    
    private fun init() {
        composeTestRule.setContent { 
            ComparaPrecosTheme {
                ResumoComparacaoListaScreen(
                    listaProduto = listaProduto,
                    listaProdutoComparada = listaProdutoComparada
                )
            }
        }
    }

    @Test
    fun shouldShowDetailsOfTwoListaCompraSelectedByUser() {
        //Given - Dado

        //When - Quando
        init()

        //Then - Entao
        composeTestRule.onNodeWithText("Lista compra").assertIsDisplayed()
        composeTestRule.onNodeWithText("3").assertIsDisplayed()
        composeTestRule.onNodeWithText("7").assertIsDisplayed()
        composeTestRule.onNodeWithText("R\$ 67,60").assertIsDisplayed()

        composeTestRule.onNodeWithText("Lista compra comparada").assertIsDisplayed()
        composeTestRule.onNodeWithText("4").assertIsDisplayed()
        composeTestRule.onNodeWithText("8").assertIsDisplayed()
        composeTestRule.onNodeWithText("R\$ 69,44").assertIsDisplayed()
    }

    @Test
    fun shouldShowDetailsOfProdutosInSameListaCompra() {
        //Given - Dado
        val listaProdutoIgual = listaProduto.second.filter { produto ->
            listaProdutoComparada.second.any { produtoComparado ->
                produto.nomeProduto == produtoComparado.nomeProduto
            }
        }
        val produtosNaMesmaLista =
            composeTestRule.activity
                .getString(R.string.label_desc_somando_produtos_nas_listas, listaProdutoIgual.size)

        //When - Quando
        init()

        //Then - Entao
        composeTestRule.onNodeWithText(produtosNaMesmaLista).assertIsDisplayed()
        composeTestRule.onNodeWithText("R\$ 67,60").assertIsDisplayed()
        composeTestRule.onNodeWithText("R\$ 58,97").assertIsDisplayed()
        composeTestRule.onNodeWithText("R\$ 8,63").assertIsDisplayed()

        composeTestRule.onNodeWithText("+14.63 %", useUnmergedTree = true)
            .assertIsDisplayed()
            .assertTextColor(CoralRed)
    }

    @Test
    fun shouldShowDetailsOfAllProdutoInTwoListaCompra() {
        //Given - Dado
        val somandoTodosOsProdutos =
            composeTestRule.activity
                .getString(R.string.label_desc_somando_todos_produtos)

        //When - Quando
        init()
        composeTestRule.onRoot().performTouchInput { swipeUp() }
//        composeTestRule.onRoot(useUnmergedTree = true).printToLog("currentTree")

        //Then - Entao
        composeTestRule.onNodeWithText(somandoTodosOsProdutos).assertIsDisplayed()
        composeTestRule.onNodeWithText("R\$ 67,60").assertIsDisplayed()
        composeTestRule.onNodeWithText("R\$ 69,44").assertIsDisplayed()
        composeTestRule.onNodeWithText("-R\$ 1,84").assertIsDisplayed()
        composeTestRule.onNodeWithText("-2.65 %", useUnmergedTree = true)
            .assertIsDisplayed()
            .assertTextColor(MediumSeaGreen)
    }
}