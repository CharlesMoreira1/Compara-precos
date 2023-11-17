package com.z1.comparaprecos.feature.listaproduto.presentation.components

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.z1.comparaprecos.common.ui.theme.ComparaPrecosTheme
import com.z1.comparaprecos.testing.data.listaProdutoDataTest
import com.z1.comparaprecos.core.common.R
import com.z1.comparaprecos.core.model.Produto
import com.z1.comparaprecos.feature.listaproduto.presentation.components.ListaProduto
import com.z1.comparaprecos.testing.data.listaProdutoDataTest2
import org.junit.Rule
import org.junit.Test

class ListaProdutoTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val listaProduto = listaProdutoDataTest

    private fun init(listaProduto: List<Produto>, listaProdutoComparada: List<Produto> = emptyList()) {
        composeTestRule.setContent {
            ComparaPrecosTheme {
                ListaProduto(
                    listaProduto = listaProduto,
                    listaProdutoComparada = listaProdutoComparada,
                    isOnTopOfList = { isElevateTopAppBar -> },
                    onProdutoClick = { produto ->  }
                )
            }
        }
    }

    @Test
    fun shouldShowListOfProdutoIfListOfProdutoIsNotEmpty() {
        //Given - Dado

        //When - Quando
        init(listaProduto)

        //Then - Entao
        composeTestRule.onNodeWithText("Arroz").assertIsDisplayed()
        composeTestRule.onNodeWithText("Feijao").assertIsDisplayed()
        composeTestRule.onNodeWithText("Banana").assertIsDisplayed()
    }

    @Test
    fun shouldMessageWhenListOfProdutoIsEmpty() {
        //Given - Dado
        val messageEmptyList = composeTestRule.activity
            .getString(R.string.label_desc_lista_produto_vazia)

        //When - Quando
        init(emptyList())

        //Then - Entao
        composeTestRule.onNodeWithText(messageEmptyList).assertIsDisplayed()
    }

    @Test
    fun shouldShowPercentageOfValueOfProdutoWhenListOfProdutoToCompararHasTheSameProdutoInTheActualList() {
        //Given - Dado

        //When - Quando
        init(listaProduto, listaProdutoDataTest2)

        composeTestRule.onRoot(useUnmergedTree = true).printToLog("currentTree")

        //Then - Entao
        composeTestRule.onNodeWithText("+4.41 %", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("+30.82 %", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("-32.36 %", useUnmergedTree = true).assertIsDisplayed()
    }
}