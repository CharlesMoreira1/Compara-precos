package com.z1.comparaprecos.feature.novalista.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.z1.comparaprecos.common.util.UiEvent
import com.z1.comparaprecos.common.util.UiText
import com.z1.comparaprecos.core.model.ListaCompraWithProdutos
import com.z1.comparaprecos.core.model.Produto
import com.z1.comparaprecos.core.common.R
import com.z1.comparaprecos.feature.novalista.domain.ProdutoUseCase
import com.z1.comparaprecos.feature.novalista.presentation.EStatusScreen
import com.z1.comparaprecos.feature.novalista.presentation.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
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
        }
    }

    private fun insertProduto(produto: Produto) = viewModelScope.launch {
        val result = isDadosProdutoCorreto(produto)
        if (result.first) {
            produtoUseCase.insertProduto(produto)
        } else {
            _uiEvent.send(UiEvent.ShowSnackbar(result.second!!))
        }
    }

    private fun updateProduto(produto: Produto) {
        TODO("Not yet implemented")
    }

    private fun deleteProduto(produto: Produto) {
        TODO("Not yet implemented")
    }

    private fun isDadosProdutoCorreto(produto: Produto): Pair<Boolean, UiText?> {
        return when {
            produto.idListaCompra <= -1 -> false to UiText.StringResource(R.string.label_peso)
            produto.nomeProduto.isBlank() -> false to UiText.StringResource(R.string.label_peso)
            produto.precoUnitario <= BigDecimal.ZERO -> false to UiText.StringResource(R.string.label_peso)
            produto.quantidade <= 0.0 -> false to UiText.StringResource(R.string.label_peso)
            else -> true to null
        }
    }

    private fun updateListaCompra(listaCompra: ListaCompraWithProdutos) {
        _uiState.update { currentState ->
            currentState.copy(
                listaCompra = listaCompra,
                screen =
                if (listaCompra.detalhes.isComparar) EStatusScreen.LISTA_COMPRA_COMPARADA
                else EStatusScreen.LISTA_COMPRA
            )
        }
    }

    private fun updateListaCompraComparada(listaCompra: ListaCompraWithProdutos) {
        _uiState.update { currentState ->
            currentState.copy(listaCompraComparada = listaCompra)
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
                if (it.detalhes.isComparar) {
                    getListaCompraComparada(it.detalhes.idListaToComparar)
                }
                updateListaCompra(it)
            } ?: _uiEvent.send(
                UiEvent.Error(
                    UiText.StringResource(R.string.label_lista_compra_nao_encontrada)
                )
            )
        }

    private fun getListaCompraComparada(idListaCompraComparada: Long) =
        viewModelScope.launch {
            val listaCompra = try {
                produtoUseCase.getListaCompra(idListaCompraComparada)
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