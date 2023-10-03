package com.z1.comparaprecos.feature.listacompra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.z1.comparaprecos.common.util.UiEvent
import com.z1.comparaprecos.common.util.UiText
import com.z1.comparaprecos.core.model.ListaCompra
import com.z1.comparaprecos.core.model.ListaCompraWithProdutos
import com.z1.comparaprecos.core.common.R
import com.z1.comparaprecos.feature.listacompra.domain.ListaCompraUseCase
import com.z1.comparaprecos.feature.listacompra.presentation.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
        when(event) {
            is OnEvent.Insert -> insertListaCompra(event.novaListaCompra)
            is OnEvent.Delete -> deletarListaCompra(event.idListaCompra)
            is OnEvent.UpdateTituloListaCompra -> updateTituloListaCompra(event.titulo)
            is OnEvent.ListaCompraSelecionada -> listaCompraSelecionada(event.listaCompra)
            is OnEvent.Reset -> resetCompra()
            is OnEvent.UpdateUiEvent -> updateUiEvent(event.uiEvent)
        }
    }

    private fun updateUiEvent(uiEvent: UiEvent) =
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }

    private fun getListaCompra() =
        viewModelScope.launch {
            listaCompraUseCase.getListaCompraWithProdutos()
                .onStart {

                }
                .onCompletion {

                }
                .catch {
                    it.printStackTrace()
                }
                .collect {
                    updateListaCompra(it)
                }
        }

    private fun insertListaCompra(novaListaCompra: ListaCompra) =
        viewModelScope.launch {
            try {
                if (validarListaCompra(novaListaCompra)) {
                    val result = listaCompraUseCase.insertNovaCompra(novaListaCompra)
                    if (result > 0) _uiEvent.send(UiEvent.Success)
                    else _uiEvent.send(UiEvent.Error(UiText.StringResource(R.string.label_desc_erro_criar_lista)))
                } else {
                    _uiEvent.send(UiEvent.ShowSnackbar(UiText.StringResource(R.string.label_desc_erro_titulo_lista)))

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    private fun deletarListaCompra(idListaCompra: Long) =
        viewModelScope.launch {
            try {
                listaCompraUseCase.deleteCompra(idListaCompra)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    private fun updateListaCompra(listaCompra: List<ListaCompraWithProdutos>) {
        _uiState.update { currentState ->
            currentState.copy(
                listaCompra = listaCompra,
                isListaCompraCarregada = true
            )
        }
    }

    private fun validarListaCompra(novaListaCompra: ListaCompra) =
        if (novaListaCompra.isTituloVazio()) {
            _uiState.update { currentState ->
                currentState.copy(isTituloVazio = true)
            }
            false
        } else true

    private fun updateTituloListaCompra(titulo: String) {
        _uiState.update { currentState ->
            currentState.copy(
                titulo = titulo,
                isTituloVazio = titulo.isBlank()
            )
        }
    }

    private fun listaCompraSelecionada(listaCompraSelecionada: ListaCompraWithProdutos?) {
        _uiState.update { currentState ->
            currentState.copy(
                listaCompraSelecionada = listaCompraSelecionada
            )
        }
    }

    private fun resetCompra() {
        _uiState.update { currentState ->
            currentState.copy(
                isTituloVazio = false,
                titulo = "",
                compararListaCompra = false,
                idListaToComparar = -1,
            )
        }
    }
}