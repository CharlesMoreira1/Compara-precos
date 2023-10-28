package com.z1.comparaprecos.feature.listacompra.presentation.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.z1.comparaprecos.common.ui.components.ETipoSnackbar
import com.z1.comparaprecos.common.util.UiText
import com.z1.comparaprecos.core.common.R
import com.z1.comparaprecos.core.model.ListaCompra
import com.z1.comparaprecos.core.model.ListaCompraWithProdutos
import com.z1.comparaprecos.core.model.Produto
import com.z1.comparaprecos.core.model.exceptions.ErrorEmptyTitle
import com.z1.comparaprecos.feature.listacompra.domain.ListaCompraUseCase
import com.z1.comparaprecos.feature.listacompra.presentation.UiEvent
import com.z1.comparaprecos.feature.listacompra.presentation.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class ListaCompraViewModel @Inject constructor(
    private val listaCompraUseCase: ListaCompraUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>(0)
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        getListaCompra()
    }

    fun onEvent(event: OnEvent) {
        when (event) {
            is OnEvent.Insert -> insertListaCompra(event.novaListaCompra)
            is OnEvent.DuplicateListaCompra -> duplicateListaCompra(
                event.novaListaCompra,
                event.listaProdutoListaCompraSelecionada
            )

            is OnEvent.UpdateListaCompra -> listaCompraCarregada(event.novaListaCompra)
            is OnEvent.Delete -> deleteListaCompra(event.idListaCompra)
            is OnEvent.UiCreateNewListaCompra -> uiCreateNewListaCompra()
            is OnEvent.UiDuplicateListaCompra -> uiDuplicateListaCompra()
            is OnEvent.UiRenameListaCompra -> uiRenameListaCompra()
            is OnEvent.UpdateTituloListaCompra -> updateTituloListaCompra(event.titulo)
            is OnEvent.ListaCompraSelecionada -> setListaCompraSelecionada(event.listaCompra)
            is OnEvent.Reset -> resetUiState()
            is OnEvent.UpdateUiEvent -> updateUiEvent(event.uiEvent)
        }
    }

    //DATABASE

    //Insert
    private fun insertListaCompra(novaListaCompra: ListaCompra) =
        viewModelScope.launch {
            try {
                val resultMessage = listaCompraUseCase.insertNovaListaCompra(novaListaCompra)
                _uiEvent.send(UiEvent.Inserted(resultMessage))
            } catch (e: ErrorEmptyTitle) {
                uiTituloVazio()
            } catch (e: Exception) {
                e.printStackTrace()
                _uiEvent.send(
                    UiEvent.ShowSnackbar(
                        UiText.StringResource(R.string.label_desc_erro_criar_lista),
                        ETipoSnackbar.ERRO
                    )
                )
            }
        }

    //Insert
    private fun duplicateListaCompra(novaListaCompra: ListaCompra, listaProduto: List<Produto>) =
        viewModelScope.launch {
            try {
                val resultMessage = listaCompraUseCase.duplicateListaCompra(novaListaCompra, listaProduto)
                _uiEvent.send(UiEvent.Inserted(resultMessage))
            } catch (e: Exception) {
                e.printStackTrace()
                _uiEvent.send(UiEvent.Error(UiText.StringResource(R.string.label_desc_erro_criar_lista)))
            }
        }

    //Get
    private fun getListaCompra() =
        viewModelScope.launch {
            listaCompraUseCase.getListaCompraWithProdutos()
                .catch {
                    it.printStackTrace()
                    listaCompraCarregada(emptyList())
                    updateUiEvent(UiEvent.Error(UiText.DynamicString(it.message ?: "")))
                }
                .collect {
                    listaCompraCarregada(it)
                }
        }

    //Update
    private fun listaCompraCarregada(listaCompra: ListaCompra) =
        viewModelScope.launch {
            try {
                val resultMessage = listaCompraUseCase.updateListaCompra(listaCompra)
                _uiEvent.send(
                    UiEvent.Updated(resultMessage)
                )
            } catch (e: Exception) {
                e.printStackTrace()
                _uiEvent.send(
                    UiEvent.ShowSnackbar(
                        UiText.StringResource(R.string.label_desc_erro_atualizar_lista),
                        ETipoSnackbar.ERRO
                    )
                )
            }
        }

    //Delete
    private fun deleteListaCompra(idListaCompra: Long) =
        viewModelScope.launch {
            try {
                val resultMessage = listaCompraUseCase.deleteCompra(idListaCompra)
                _uiEvent.send(UiEvent.Deleted(resultMessage))
            } catch (e: Exception) {
                e.printStackTrace()
                _uiEvent.send(
                    UiEvent.ShowSnackbar(
                        UiText.StringResource(R.string.label_erro_excluir_lista_compra),
                        ETipoSnackbar.ERRO
                    )
                )
            }
        }
    //DATABASE

    //UISTATE

    private fun updateUiEvent(uiEvent: UiEvent) =
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }

    private fun uiCreateNewListaCompra() =
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    tituloBottomSheet = R.string.label_nova_lista_compra,
                    descricaoBottomSheet = R.string.label_desc_nova_lista_compra,
                    buttonBottomSheet = R.string.label_criar_lista_compra,
                    tituloListaCompra = ""
                )
            }
            _uiEvent.send(UiEvent.ShowBottomSheet)
        }

    private fun uiDuplicateListaCompra() =
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    tituloBottomSheet = R.string.label_duplicar_lista,
                    descricaoBottomSheet = R.string.label_desc_duplicar_lista,
                    buttonBottomSheet = R.string.label_duplicar_lista_compra,
                    isDuplicarListaCompra = true,
                    tituloListaCompra = ""
                )
            }
            _uiEvent.send(UiEvent.ShowBottomSheet)
        }

    private fun uiRenameListaCompra() =
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    tituloBottomSheet = R.string.label_renomear_lista,
                    descricaoBottomSheet = R.string.label_desc_renomear_lista_compra,
                    buttonBottomSheet = R.string.label_renomear_lista_compra,
                    isRenomearListaCompra = true,
                    tituloListaCompra = currentState.listaCompraSelecionada?.detalhes?.titulo ?: ""
                )
            }
            _uiEvent.send(UiEvent.ShowBottomSheet)
        }

    private fun uiTituloVazio() {
        _uiState.update { currentState ->
            currentState.copy(isTituloVazio = true)
        }
    }

    private fun listaCompraCarregada(listaCompra: List<ListaCompraWithProdutos>) {
        _uiState.update { currentState ->
            currentState.copy(
                listaCompra = listaCompra,
                isListaCompraCarregada = true
            )
        }
    }

    private fun updateTituloListaCompra(titulo: String) {
        _uiState.update { currentState ->
            currentState.copy(
                tituloListaCompra = titulo,
                isTituloVazio = titulo.isBlank()
            )
        }
    }

    private fun setListaCompraSelecionada(listaCompraSelecionada: ListaCompraWithProdutos?) =
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    listaCompraSelecionada = listaCompraSelecionada
                )
            }
            _uiEvent.send(UiEvent.ShowBottomSheetOptions)
        }

    private fun resetUiState() {
        _uiState.update { currentState ->
            currentState.copy(
                isTituloVazio = false,
                isDuplicarListaCompra = false,
                isRenomearListaCompra = false,
                tituloListaCompra = ""
            )
        }
    }

    //UISTATE

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun collectUiEvent(): UiEvent {
        return runBlocking {
            _uiEvent.receive()
        }
    }
}