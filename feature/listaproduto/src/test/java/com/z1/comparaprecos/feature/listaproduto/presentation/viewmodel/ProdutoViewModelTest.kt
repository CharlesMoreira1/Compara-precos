package com.z1.comparaprecos.feature.listaproduto.presentation.viewmodel

import com.z1.comparaprecos.core.common.R
import com.z1.comparaprecos.core.model.Produto
import com.z1.comparaprecos.core.model.exceptions.ErrorDelete
import com.z1.comparaprecos.core.model.exceptions.ErrorEmptyList
import com.z1.comparaprecos.core.model.exceptions.ErrorProductData
import com.z1.comparaprecos.core.model.exceptions.ErrorProductExists
import com.z1.comparaprecos.feature.listaproduto.domain.ProdutoUseCase
import com.z1.comparaprecos.feature.listaproduto.presentation.state.UiEvent
import com.z1.comparaprecos.testing.BaseTest
import com.z1.comparaprecos.testing.data.listaCompraOptionsDataTest
import com.z1.comparaprecos.testing.data.listaCompraWithProductTestData
import com.z1.comparaprecos.testing.data.listaProdutoDataTest
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal

class ProdutoViewModelTest: BaseTest() {
    private lateinit var viewModel: ProdutoViewModel
    private lateinit var produto: Produto
    private val useCase: ProdutoUseCase = mockk()

    @Before
    override fun beforeEach() {
        super.beforeEach()
        viewModel = ProdutoViewModel(useCase)
        produto = Produto(
            0L,
            0,
            "Teste",
            "1",
            BigDecimal("1.99"),
            false
        )
    }

    @After
    override fun afterEach() {
        super.afterEach()
        clearAllMocks()
    }

    //Get listaCompra
    @Test
    fun `should return listaCompra when user start screen with given id`() {
        //Given - Dado
        coEvery { useCase.getListaCompra(0) } returns listaCompraWithProductTestData[0].detalhes

        //When - Quando
        viewModel.onEvent(OnEvent.GetListaCompra(0))
        val listaCompra = viewModel.uiState.value.listaCompra

        //Then - Entao
        assertTrue(listaCompra.titulo == "ListaCompra 0")
    }

    @Test
    fun `should return UiEvent_Error message when some error occur getting listaCompra`() {
        //Given - Dado
        coEvery { useCase.getListaCompra(0) } throws Exception()

        //When - Quando
        viewModel.onEvent(OnEvent.GetListaCompra(0))
        val currentUiEvent = viewModel.collectUiEvent()

        //Then - Entao
        assertTrue(currentUiEvent is UiEvent.Error)
    }
    //Get listaCompra

    //Get listaCompra options
    @Test
    fun `should return all listaCompra options when user click add listaCompra to compare button`() {
        //Given - Dado
        coEvery { useCase.getListaCompraOptions(-1) } returns listaCompraOptionsDataTest

        //When - Quando
        viewModel.onEvent(OnEvent.GetListaCompraOptions)
        val currentList = viewModel.uiState.value.listaCompraOptions

        //Then - Entao
        assertTrue(currentList.isNotEmpty())
    }

    @Test
    fun `should return ErrorEmptyList when no listaCompra options found`() {
        //Given - Dado
        coEvery { useCase.getListaCompraOptions(-1) } throws ErrorEmptyList()

        //When - Quando
        viewModel.onEvent(OnEvent.GetListaCompraOptions)
        val currentUiEvent = viewModel.collectUiEvent()

        //Then - Entao
        assertTrue(currentUiEvent is UiEvent.ShowSnackbar)
    }

    //Get listaCompra options

    //Get listaCompra to comparar
    @Test
    fun `should return a listaCompraWithProdutos when user selected a listaCompra to comparar`() {
        //Given - Dado
        coEvery { useCase.getListaCompraComparada(1) } returns listaCompraWithProductTestData[1]

        //When - Quando
        viewModel.onEvent(OnEvent.GetListaCompraToComparar(1))
        val listaCompraToComparar = viewModel.uiState.value.listaCompraComparada

        assertTrue(listaCompraToComparar.detalhes.titulo == listaCompraWithProductTestData[1].detalhes.titulo)
    }

    @Test
    fun `should return UiEvent_Error when not found a listaCompra to comparar`() {
        //Given - Dado
        coEvery { useCase.getListaCompraComparada(1) } throws Exception()

        //When - Quando
        viewModel.onEvent(OnEvent.GetListaCompraToComparar(1))
        val currentUiEvent = viewModel.collectUiEvent()

        assertTrue(currentUiEvent is UiEvent.Error)
    }
    //Get listaCompra to comparar

    //Get list
    @Test
    fun `should return a list of Produto when find a listaCompra`() {
        //Given - Dado
        coEvery { useCase.getListaCompra(0) } returns listaCompraWithProductTestData[0].detalhes
        coEvery { useCase.getListaProduto(0) } returns flowOf(listaProdutoDataTest)

        //When - Quando
        viewModel.onEvent(OnEvent.GetListaCompra(0))
        val currentList = viewModel.uiState.value.listaProduto

        //Then - Entao
        assertTrue(currentList.isNotEmpty())
    }

    @Test
    fun `should return UiEvent_Error when some error occur getting list of Produto`() {
        //Given - Dado
        coEvery { useCase.getListaCompra(0) } returns listaCompraWithProductTestData[0].detalhes
        coEvery { useCase.getListaProduto(0) } returns flow { throw Exception() }

        //When - Quando
        viewModel.onEvent(OnEvent.GetListaCompra(0))
        val currentUiEvent = viewModel.collectUiEvent()

        //Then - Entao
        assertTrue(currentUiEvent is UiEvent.Error)
    }
    //Get list

    //Insert
    @Test
    fun `should return UiEvent_Success when user insert a new Produto`() {
        //Given - Dado
        coEvery { useCase.insertProduto(produto, emptyList()) } returns 0

        //When - Quando
        viewModel.onEvent(OnEvent.InsertProduto(produto))
        val currentUiEvent = viewModel.collectUiEvent()

        //Then - Entao
        assertTrue(currentUiEvent is UiEvent.Success)
    }

    @Test
    fun `should return ErrorProductExists when user try add an existing Product`() {
        //Given - Dado
        coEvery { useCase.insertProduto(produto, emptyList()) } throws ErrorProductExists()

        //When - Quando
        viewModel.onEvent(OnEvent.InsertProduto(produto))
        val currenUiEvent = viewModel.collectUiEvent()

        //Then - Entao
        assertTrue(currenUiEvent is UiEvent.Error)
    }

    @Test
    fun `should return ErrorProductData when user try add a Product with incorrect data`() {
        //Given - Dado
        coEvery { useCase.insertProduto(produto, emptyList()) } throws ErrorProductData(uiMessageId = R.string.label_informe_nome_produto)

        //When - Quando
        viewModel.onEvent(OnEvent.InsertProduto(produto))
        val currenUiEvent = viewModel.collectUiEvent()

        //Then - Entao
        assertTrue(currenUiEvent is UiEvent.ShowSnackbar)
    }

    @Test
    fun `should return error when not inserted a produto `() {
        //Given - Dado
        coEvery { useCase.insertProduto(produto, emptyList()) } throws Exception()

        //When - Quando
        viewModel.onEvent(OnEvent.InsertProduto(produto))
        val currenUiEvent = viewModel.collectUiEvent()

        //Then - Entao
        assertTrue(currenUiEvent is UiEvent.ShowSnackbar)
    }
    //Insert

    //Update
    @Test
    fun `should return UiEvent_ShowSnackbar when user update a produto`() {
        //Given - Dado
        coEvery { useCase.updateProduto(produto) } returns 0

        //When - Quando
        viewModel.onEvent(OnEvent.UpdateProduto(produto))
        val curreUiEvent = viewModel.collectUiEvent()

        //Then - Entao
        assertTrue(curreUiEvent is UiEvent.ShowSnackbar)
    }

    @Test
    fun `should return error when not update a produto`() {
        //Given - Dado
        coEvery { useCase.updateProduto(produto) } throws Exception()

        //When - Quando
        viewModel.onEvent(OnEvent.UpdateProduto(produto))
        val curreUiEvent = viewModel.collectUiEvent()

        //Then - Entao
        assertTrue(curreUiEvent is UiEvent.Error)
    }
    //Update

    //Delete
    @Test
    fun `should return UiEvent_ShowSnackbar when user delete a produto`() {
        //Given - Dado
        coEvery { useCase.deleteProduto(produto) } returns 0

        //When - Quando
        viewModel.onEvent(OnEvent.DeleteProduto(produto))
        val currentUiEvent = viewModel.collectUiEvent()

        //Then - Entao
        assertTrue(currentUiEvent is UiEvent.ShowSnackbar)
    }

    @Test
    fun `should return UiEvent_ShowSnackbar when not delete a produto`() {
        //Given - Dado
        coEvery { useCase.deleteProduto(produto) } throws ErrorDelete()

        //When - Quando
        viewModel.onEvent(OnEvent.DeleteProduto(produto))
        val currentUiEvent = viewModel.collectUiEvent()

        //Then - Entao
        assertTrue(currentUiEvent is UiEvent.ShowSnackbar)
    }
    //Delete

    //UiState
    @Test
    fun `should return a produto when user select a produto`() {
        //Given - Dado

        //When - Quando
        viewModel.onEvent(OnEvent.ProdutoSelecionado(produto))
        val uiState = viewModel.uiState.value
        //Then - Entao
        assertTrue(uiState.produtoSelecionado?.nomeProduto == produto.nomeProduto)
    }

    @Test
    fun `should return new quantity when user update quantity of an exiting produto`() {
        //Given - Dado
        var produto = listaProdutoDataTest[0]

        coEvery { useCase.getListaCompra(0) } returns listaCompraWithProductTestData[0].detalhes
        coEvery { useCase.getListaProduto(0) } returns flowOf(listaProdutoDataTest)
        coEvery { useCase.updateProduto(produto.copy(quantidade = "2")) } coAnswers {
            produto = listaProdutoDataTest[0].copy(quantidade = "2")
            0
        }

        //When - Quando
        viewModel.onEvent(OnEvent.GetListaCompra(0))
        viewModel.onEvent(OnEvent.UpdateQuantidadeProdutoExistente(produto))

        //Then - Entao
        assertEquals("2", produto.quantidade)
    }

    @Test
    fun `should return UiEvent_Default when user cancel bottomsheetdialog about existing product`() {
        //Given - Dado
        coEvery { useCase.getListaCompra(0) } returns listaCompraWithProductTestData[0].detalhes
        coEvery { useCase.getListaProduto(0) } returns flowOf(listaProdutoDataTest)

        //When - Quando
        viewModel.onEvent(OnEvent.UpdateQuantidadeProdutoExistente(null))
        val currentUiEvent = viewModel.collectUiEvent()

        //Then - Entao
        assertTrue(currentUiEvent is UiEvent.Default)
    }

    @Test
    fun `should return UiEvent_Default when update UiEvent`() {
        //When - Quando
        viewModel.onEvent(OnEvent.UpdateUiEvent(UiEvent.Default))
        val currentUiEvent = viewModel.collectUiEvent()

        //Then - Entao
        assertTrue(currentUiEvent is UiEvent.Default)
    }
    //UiState
}