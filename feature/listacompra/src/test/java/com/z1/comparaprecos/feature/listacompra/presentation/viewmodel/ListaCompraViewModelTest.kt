package com.z1.comparaprecos.feature.listacompra.presentation.viewmodel

import com.z1.comparaprecos.core.common.R
import com.z1.comparaprecos.core.model.ListaCompra
import com.z1.comparaprecos.core.model.exceptions.ErrorEmptyTitle
import com.z1.comparaprecos.core.model.exceptions.ErrorInsert
import com.z1.comparaprecos.feature.listacompra.domain.ListaCompraUseCase
import com.z1.comparaprecos.feature.listacompra.presentation.UiEvent
import com.z1.comparaprecos.feature.listacompra.presentation.UiState
import com.z1.comparaprecos.testing.BaseTest
import com.z1.comparaprecos.testing.data.listaCompraWithProductTestData
import com.z1.comparaprecos.testing.data.listaProdutoDataTest
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


class ListaCompraViewModelTest: BaseTest() {

    private lateinit var viewModel: ListaCompraViewModel
    private lateinit var listaCompra: ListaCompra
    private val usecase: ListaCompraUseCase = mockk(relaxed = true)

    @Before
    override fun beforeEach() {
        super.beforeEach()
        viewModel = ListaCompraViewModel(usecase)
        listaCompra = ListaCompra(0, "Teste", 0L)
    }

    @After
    override fun afterEach() {
        super.afterEach()
        clearAllMocks()
    }

    //Get List
    @Test
    fun `should return list of listaCompra when viewmodel initialized`() {
        //Given - Dado
        coEvery { usecase.getListaCompraWithProdutos() } returns flowOf(listaCompraWithProductTestData)

        //When - Quando
        viewModel = ListaCompraViewModel(usecase)
        val currentList = viewModel.uiState.value.listaCompra

        //Then - Entao
        assertTrue(currentList.isNotEmpty())
        coVerify { usecase.getListaCompraWithProdutos() }
    }

    @Test
    fun `should return empty list of listaCompra when no data in database`() {
        //Given - Dado
        coEvery { usecase.getListaCompraWithProdutos() } returns flowOf(emptyList())

        //When - Quando
        viewModel = ListaCompraViewModel(usecase)
        val currentList = viewModel.uiState.value.listaCompra

        //Then - Entao
        assertTrue(currentList.isEmpty())

        coVerify { usecase.getListaCompraWithProdutos() }
    }

    @Test
    fun `should return UiEvent_Error when some error occur getting list`() {
        //Given - Dado
        coEvery { usecase.getListaCompraWithProdutos() } returns  flow { throw Exception() }

        //When - Quando
        viewModel = ListaCompraViewModel(usecase)
        val currentUiEvent = viewModel.collectUiEvent()

        //Then - Entao
        assertTrue(currentUiEvent is UiEvent.Error)

        coVerify { usecase.getListaCompraWithProdutos() }
    }
    //Get List

    //Insert new item
    @Test
    fun `should return UiEvent_Inserted when insert new listaCompra with valid data`() {
        //Given - Dado
        coEvery { usecase.insertNovaListaCompra(any()) } returns R.string.label_desc_lista_compra_criada

        //When - Quando
        viewModel.onEvent(OnEvent.Insert(listaCompra))
        val currentUiEvent = viewModel.collectUiEvent()

        //Then - Entao
        assertTrue(currentUiEvent is UiEvent.Inserted)
    }

    @Test
    fun `should return ErrorEmptyTitle when insert new listaCompra with empty title`() {
        //Given - Dado
        val listaCompra = ListaCompra(0, "", 0L)
        coEvery { usecase.insertNovaListaCompra(any()) } throws ErrorEmptyTitle()

        //When - Quando
        viewModel.onEvent(OnEvent.Insert(listaCompra))
        val isTituloVazio = viewModel.uiState.value.isTituloVazio

        //Then - Entao
        assertTrue(isTituloVazio)
    }

    @Test
    fun `should return ErrorInsert when insert new listaCompra getting some error`() {
        //Given - Dado
        coEvery { usecase.insertNovaListaCompra(any()) } throws ErrorInsert()

        //When - Quando
        viewModel.onEvent(OnEvent.Insert(listaCompra))
        val currentUiEvent = viewModel.collectUiEvent()

        //Then - Entao
        assertTrue(currentUiEvent is UiEvent.ShowSnackbar)
    }
    //Insert new item

    //Duplicate item
    @Test
    fun `should return UiEvent_Inserted when duplicate a new item`() {
        //Given - Dado

        val listaProduto = listaProdutoDataTest
        coEvery { usecase.duplicateListaCompra(listaCompra, listaProduto) } returns R.string.label_lista_compra_duplicada

        //When - Quando
        viewModel.onEvent(OnEvent.DuplicateListaCompra(listaCompra, listaProduto))
        val currentUiEvent = viewModel.collectUiEvent()

        assertTrue(currentUiEvent is UiEvent.Inserted)

        coVerify { usecase.duplicateListaCompra(listaCompra, listaProduto) }
    }

    @Test
    fun `should return UiEvent_Error when not duplicate a new item`() {
        //Given - Dado
        val listaProduto = listaProdutoDataTest
        coEvery { usecase.duplicateListaCompra(listaCompra, listaProduto) } throws Exception()

        //When - Quando
        viewModel.onEvent(OnEvent.DuplicateListaCompra(listaCompra, listaProduto))
        val currentUiEvent = viewModel.collectUiEvent()

        assertTrue(currentUiEvent is UiEvent.Error)

        coVerify { usecase.duplicateListaCompra(listaCompra, listaProduto) }
    }
    //Duplicate item

    //Update item
    @Test
    fun `should return UiEvent_Updated when update a listaCompra`() {
        //Given - Dado
        coEvery { usecase.updateListaCompra(listaCompra) } returns R.string.label_lista_compra_duplicada

        //When - Entao
        viewModel.onEvent(OnEvent.UpdateListaCompra(listaCompra))
        val currentUiEvent = viewModel.collectUiEvent()

        //Then - Entao
        assertTrue(currentUiEvent is UiEvent.Updated)

        coVerify { usecase.updateListaCompra(listaCompra) }
    }

    @Test
    fun `should return UiEvent_ShowSnackbar when some error occur updating a listaCompra`() {
        //Given - Dado
        coEvery { usecase.updateListaCompra(listaCompra) } throws Exception()

        //When - Entao
        viewModel.onEvent(OnEvent.UpdateListaCompra(listaCompra))
        val currentUiEvent = viewModel.collectUiEvent()

        //Then - Entao
        assertTrue(currentUiEvent is UiEvent.ShowSnackbar)

        coVerify { usecase.updateListaCompra(listaCompra) }
    }
    //Update item

    //Delete item
    @Test
    fun `should return UiEvent_Deleted when delete a listaCompra`() {
        //Given - Dado
        coEvery { usecase.deleteListaCompra(0) } returns R.string.label_lista_compra_excluida

        //When - Entao
        viewModel.onEvent(OnEvent.Delete(0))
        val currentUiEvent = viewModel.collectUiEvent()

        //Then - Entao
        assertTrue(currentUiEvent is UiEvent.Deleted)

        coVerify { usecase.deleteListaCompra(0) }
    }

    @Test
    fun `should return UiEvent_ShowSnackbar when some error occur deleting a listaCompra`() {
        //Given - Dado
        coEvery { usecase.deleteListaCompra(0) } throws Exception()

        //When - Entao
        viewModel.onEvent(OnEvent.Delete(0))
        val currentUiEvent = viewModel.collectUiEvent()

        //Then - Entao
        assertTrue(currentUiEvent is UiEvent.ShowSnackbar)

        coVerify { usecase.deleteListaCompra(0) }
    }
    //Delete item

    //Create new listaCompra
    @Test
    fun `should return UiEvent_ShowBottomSheet when user click on new listaCompra button`() {
        //Given - Dado

        //When - Quando
        viewModel.onEvent(OnEvent.UiCreateNewListaCompra)
        val uiState = viewModel.uiState.value
        val currentUiEvent = viewModel.collectUiEvent()

        //Then - Entao
        assertTrue(currentUiEvent is UiEvent.ShowBottomSheet)
        assertTrue(uiState.tituloBottomSheet == R.string.label_nova_lista_compra)
    }

    //Create new listaCompra

    //Duplicate listaCompra
    @Test
    fun `should return UiEvent_ShowBottomSheet when user click on duplicate listaCompra option`() {
        //Given - Dado

        //When - Quando
        viewModel.onEvent(OnEvent.UiDuplicateListaCompra)
        val uiState = viewModel.uiState.value
        val currentUiEvent = viewModel.collectUiEvent()

        //Then - Entao
        assertTrue(currentUiEvent is UiEvent.ShowBottomSheet)
        assertTrue(uiState.tituloBottomSheet == R.string.label_duplicar_lista)
    }
    //Duplicate listaCompra

    //Rename listaCompra
    @Test
    fun `should return UiEvent_ShowBottomSheet when user click on rename listaCompra option`() {
        //Given - Dado

        //When - Quando
        viewModel.onEvent(OnEvent.UiRenameListaCompra)
        val uiState = viewModel.uiState.value
        val currentUiEvent = viewModel.collectUiEvent()

        //Then - Entao
        assertTrue(currentUiEvent is UiEvent.ShowBottomSheet)
        assertTrue(uiState.tituloBottomSheet == R.string.label_renomear_lista)
    }
    //Rename listaCompra

    //Update title listaCompra
    @Test
    fun `should update listaCompra title when user is typing`() {
        //Given - Dado
        val tituloAnterior = viewModel.uiState.value.tituloListaCompra
        val isTituloVazioAnterior = viewModel.uiState.value.isTituloVazio

        //When - Quando
        viewModel.onEvent(OnEvent.UpdateTituloListaCompra("Nova Lista"))
        val tituloAtual = viewModel.uiState.value.tituloListaCompra
        val isTituloVazioAtual = viewModel.uiState.value.isTituloVazio


        //Then - Entao
        assertTrue(tituloAnterior != tituloAtual)
        assertTrue(tituloAnterior.isBlank())
        assertTrue(tituloAtual.isNotBlank())
        assertFalse(isTituloVazioAnterior)
        assertFalse(isTituloVazioAtual)
    }
    //Update title listaCompra

    //Select listaCompra
    @Test
    fun `should set a new listaCompra when user select other listaCompra in the list`() {
        //Given - Dado
        val listaCompraAnterior = listaCompraWithProductTestData[0]
        viewModel.onEvent(OnEvent.ListaCompraSelecionada(listaCompraAnterior))

        //When - Quando
        val listaCompraAtual = listaCompraWithProductTestData[1]
        viewModel.onEvent(OnEvent.ListaCompraSelecionada(listaCompraAtual))

        val listaCompra = viewModel.uiState.value.listaCompraSelecionada

        //Then - Entao
        assertTrue(listaCompra?.detalhes?.titulo == listaCompraAtual.detalhes.titulo)
    }
    //Select listaCompra

    //Reset uiState
    @Test
    fun `should reset uiState when hide bottom sheet`() {
        //Given - Dado
        val uiStateAnteriror = UiState(
            isTituloVazio = true,
            isDuplicarListaCompra = true,
            isRenomearListaCompra = true,
            tituloListaCompra = "Lista compra"
        )

        //When - Quando
        viewModel.onEvent(OnEvent.Reset)
        val uiStateAtual = viewModel.uiState.value

        //Then - Entao
        assertTrue(uiStateAnteriror.tituloListaCompra != uiStateAtual.tituloListaCompra)
        assertFalse(uiStateAtual.isTituloVazio)
        assertFalse(uiStateAtual.isDuplicarListaCompra)
        assertFalse(uiStateAtual.isRenomearListaCompra)
    }
    //Reset uiState

    //Update UiEvent
    @Test
    fun `should update uiEvent when another event is emitted`() {
        //Given - Dado
        val uiEventeAnterior = UiEvent.Default

        //When - Quando
        viewModel.onEvent(OnEvent.UpdateUiEvent(UiEvent.HideBottomSheetOptions))
        val uiEventAtual = viewModel.collectUiEvent()

        //Then - Entao
        assertTrue(uiEventAtual != uiEventeAnterior)
        assertTrue(uiEventAtual is UiEvent.HideBottomSheetOptions)
    }
    //Update UiEvent
}