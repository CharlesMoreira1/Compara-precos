package com.z1.comparaprecos.feature.novalista.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.z1.comparaprecos.common.util.UiEvent
import com.z1.comparaprecos.common.util.UiText
import com.z1.comparaprecos.core.model.ListaCompraWithProdutos
import com.z1.comparaprecos.core.model.Produto
import com.z1.comparaprecos.core.common.R
import com.z1.comparaprecos.core.model.ListaCompra
import com.z1.comparaprecos.feature.novalista.domain.ProdutoUseCase
import com.z1.comparaprecos.feature.novalista.presentation.EStatusScreen
import com.z1.comparaprecos.feature.novalista.presentation.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class ProdutoViewModel @Inject constructor(
    private val produtoUseCase: ProdutoUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        initialValue = _uiState.value
    )

    private val _uiEvent = Channel<UiEvent>(0)
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: OnEvent) {
        when (event) {
            is OnEvent.GetListaCompra -> getListaCompra(event.idListaCompra)
            is OnEvent.InsertProduto -> insertProduto(event.produto)
            is OnEvent.UpdateProduto -> updateProduto(event.produto)
            is OnEvent.DeleteProduto -> deleteProduto(event.produto)
            is OnEvent.ProdutoSelecionado -> updateProdutoSelecionado(event.produto)
            is OnEvent.UpdateUiEvent -> updateUiEvent(event.uiEvent)
        }
    }

    private fun updateUiEvent(uiEvent: UiEvent) =
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }

    private fun insertProduto(produto: Produto) = viewModelScope.launch {
        val result = isDadosProdutoCorreto(produto)
        if (result.first) {
            val isProdutoAdiconado = produtoUseCase.insertProduto(produto)
            _uiEvent.send(if (isProdutoAdiconado > 0) UiEvent.Success else UiEvent.Default)
        } else {
            _uiEvent.send(UiEvent.ShowSnackbar(result.second!!))
        }
    }

    private fun updateProduto(produto: Produto) =
        viewModelScope.launch {
            val result = produtoUseCase.updateProduto(produto)
            if (result > 0) {
                updateProdutoSelecionado()
                _uiEvent.send(UiEvent.ShowSnackbar(UiText.StringResource(R.string.label_produto_editado)))
            }
            else _uiEvent.send(UiEvent.Error(UiText.StringResource(R.string.label_desc_erro_editar_produto)))
        }

    private fun deleteProduto(produto: Produto) =
        viewModelScope.launch {
            val result = produtoUseCase.deleteProduto(produto)
            _uiEvent.send(
                if (result > 0) UiEvent.ShowSnackbar(UiText.StringResource(R.string.label_produto_removido))
                else UiEvent.ShowSnackbar(UiText.StringResource(R.string.label_desc_erro_excluir_produto))
            )
        }

    private fun updateProdutoSelecionado(produto: Produto? = null) {
        _uiState.update { currentState ->
            currentState.copy(produtoSelecionado = produto)
        }
    }

    private fun isDadosProdutoCorreto(produto: Produto): Pair<Boolean, UiText?> {
        return when {
            produto.idListaCompra <= -1 -> false to UiText.StringResource(R.string.label_peso)
            produto.nomeProduto.isBlank() -> false to UiText.StringResource(R.string.label_peso)
            produto.precoUnitario <= BigDecimal.ZERO -> false to UiText.StringResource(R.string.label_peso)
            produto.quantidade.toDouble() <= 0.0 -> false to UiText.StringResource(R.string.label_peso)
            else -> true to null
        }
    }

    private fun updateListaCompra(listaCompra: ListaCompra) {
        _uiState.update { currentState ->
            currentState.copy(
                listaCompra = listaCompra,
                screen =
                if (listaCompra.isComparar) EStatusScreen.LISTA_COMPRA_COMPARADA
                else EStatusScreen.LISTA_COMPRA
            )
        }
    }

    private fun updateListaCompraComparada(listaCompra: ListaCompraWithProdutos) {
        _uiState.update { currentState ->
            currentState.copy(listaCompraComparada = listaCompra)
        }
    }

    private fun updateListaProduto(listaProduto: List<Produto>) {
        _uiState.update { currentState ->
            currentState.copy(listaProduto = listaProduto)
        }
    }

    private fun getListaCompra(idListaCompra: Long) =
        viewModelScope.launch {
            val listaCompra = try {
                produtoUseCase.getListaCompra(idListaCompra)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }

            listaCompra?.let {
                if (it.isComparar) {
                    getListaCompraComparada(it.idListaToComparar)
                }
                updateListaCompra(it)
                getListaProduto(it.id)
            } ?: _uiEvent.send(
                UiEvent.Error(
                    UiText.StringResource(R.string.label_lista_compra_nao_encontrada)
                )
            )
        }

    private fun getListaProduto(idListaCompra: Long) =
        viewModelScope.launch {
            produtoUseCase.getListaProduto(idListaCompra)
                .onStart {

                }
                .onCompletion {

                }
                .catch {
                    _uiEvent.send(UiEvent.Error(UiText.StringMessage(it.message ?: "")))
                }
                .collect {
                    updateListaProduto(it)
                }
        }

    private fun getListaCompraComparada(idListaCompraComparada: Long) =
        viewModelScope.launch {
            val listaCompra = try {
                produtoUseCase.getListaCompraComparada(idListaCompraComparada)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }

            listaCompra?.let {
                updateListaCompraComparada(it)
            } ?: _uiEvent.send(
                UiEvent.Error(
                    UiText.StringResource(R.string.label_lista_compra_comparada_nao_encontrada)
                )
            )
        }
}