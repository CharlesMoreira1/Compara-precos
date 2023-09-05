package com.z1.comparaprecos.feature.listacompra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.z1.comparaprecos.common.ui.components.Mensagem
import com.z1.comparaprecos.core.model.ListaCompra
import com.z1.comparaprecos.feature.listacompra.domain.CompraUseCase
import com.z1.comparaprecos.feature.listacompra.presentation.ELoanding
import com.z1.comparaprecos.feature.listacompra.presentation.EStatusListaCompra
import com.z1.comparaprecos.feature.listacompra.presentation.ETypeErrors
import com.z1.comparaprecos.feature.listacompra.presentation.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListaCompraViewModel @Inject constructor(
    private val compraUseCase: CompraUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        getListaCompra()
    }

    private fun insertNovaCompra(novaListaCompra: ListaCompra) =
        viewModelScope.launch {
            try {
                updtateStatusLoading(ELoanding.CRIANDO_COMPRA)
                compraUseCase.insertNovaCompra(novaListaCompra)
                updtateStatusCompra(EStatusListaCompra.CRIADA)
            } catch (e: Exception) {
                updtateStatusCompra(EStatusListaCompra.ERRO_AO_CRIAR)
                e.message?.let { updateError(ETypeErrors.ERRO_INSERIR_LISTA, it) }
                e.printStackTrace()
            }
        }

    private fun getListaCompra() =
        viewModelScope.launch {
            compraUseCase.getListaCompra()
                .onStart {
                    updtateStatusLoading(ELoanding.AGUARDE)
                }
                .onCompletion {
                    updtateStatusLoading(ELoanding.NOTHING)
                }
                .catch {
                    it.message?.let { message ->
                        updateError(ETypeErrors.ERRO_CARREGAR_LISTA, message)
                    }
                    it.printStackTrace()
                }
                .collect {
                    updateListaCompra(it)
                    updtateStatusLoading(ELoanding.NOTHING)
                }
        }

    fun deletarListaCompra(idListaCompra: Long) =
        viewModelScope.launch {
            try {
                updtateStatusLoading(ELoanding.AGUARDE)
                compraUseCase.deleteCompra(idListaCompra)
                updtateStatusCompra(EStatusListaCompra.EXCLUIDA)
            } catch (e: Exception) {
                updtateStatusLoading(ELoanding.NOTHING)
                updtateStatusCompra(EStatusListaCompra.ERRO_AO_CRIAR)
                e.message?.let { updateError(ETypeErrors.ERRO_INSERIR_LISTA, it) }
                e.printStackTrace()
            }
        }

    private fun updtateStatusLoading(isLoading: ELoanding) {
        _uiState.update { currentState ->
            currentState.copy(isLoading = isLoading)
        }
    }

    private fun updtateStatusCompra(statusListaCompra: EStatusListaCompra) {
        _uiState.update { currentState ->
            currentState.copy(statusListaCompra = statusListaCompra)
        }

        when (statusListaCompra) {
            EStatusListaCompra.CRIADA,
            EStatusListaCompra.ERRO_AO_CRIAR,
            EStatusListaCompra.EDITADA,
            EStatusListaCompra.ERRO_AO_EDITAR,
            EStatusListaCompra.EXCLUIDA,
            EStatusListaCompra.ERRO_AO_EXCLUIR -> updtateStatusLoading(ELoanding.NOTHING)
            else -> Unit
        }
    }

    private fun updateListaCompra(listaCompra: List<ListaCompra>) {
        _uiState.update { currentState ->
            currentState.copy(listaCompra = listaCompra)
        }
    }

    fun validarNovaCompra(novaListaCompra: ListaCompra) {

        if (novaListaCompra.isNotTituloValido()) {
            updateError(ETypeErrors.TITULO_VAZIO)
            return
        }

        if (novaListaCompra.isComparar) {
            if (novaListaCompra.isNotIdListaToComparar()) {
                updateError(
                    ETypeErrors.LISTA_TO_COMPARAR_VAZIA,
                    "Selecione a lista que deseja comparar."
                )
                return
            }
        }
        insertNovaCompra(novaListaCompra)
    }

    fun updateError(isError: ETypeErrors? = null, message: String = "") {
        _uiState.update { currentState ->
            currentState.copy(isError = isError to message)
        }
    }

    fun updateCompararCompra(state: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(compararListaCompra = state)
        }
    }

    fun updateTitulo(titulo: String) {
        _uiState.update { currentState ->
            currentState.copy(titulo = titulo)
        }

        if (titulo.isNotEmpty()) updateError()
    }

    fun updateCompraSelecionada(listaCompraSelecionada: ListaCompra?) {
        _uiState.update { currentState ->
            currentState.copy(
                listaListaCompraSelecionada = listaCompraSelecionada,
                statusListaCompra = EStatusListaCompra.NOTHING
            )
        }
    }

    fun updateIdListaToComparar(idListaToComparar: Long) {
        _uiState.update { currentState ->
            currentState.copy(idListaToComparar = idListaToComparar)
        }
    }

    fun addMensagem(novaMensagem: Mensagem) {
        val novaLista = _uiState.value.listaMensagem
        novaLista.add(novaMensagem)
        _uiState.update { currentState ->
            currentState.copy(
                listaMensagem = novaLista,
                isShowingSnackBar = true
            )
        }
    }

    fun removerMensagem(mensagem: Mensagem) {
        val novaLista = _uiState.value.listaMensagem
        novaLista.remove(mensagem)
        _uiState.update { currentState ->
            currentState.copy(
                listaMensagem = novaLista,
                isShowingSnackBar = novaLista.isNotEmpty()
            )
        }
    }

    fun resetCompra() {
        _uiState.update { currentState ->
            currentState.copy(
                isLoading = ELoanding.NOTHING,
                isErrorTitulo = false,
                titulo = "",
                compararListaCompra = false,
                idListaToComparar = -1,
                statusListaCompra = EStatusListaCompra.NOTHING
            )
        }
    }
}