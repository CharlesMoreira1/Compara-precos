package com.z1.comparaprecos.feature.listaproduto.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.z1.comparaprecos.common.util.UiEvent
import com.z1.comparaprecos.common.util.UiText
import com.z1.comparaprecos.core.model.ListaCompraWithProdutos
import com.z1.comparaprecos.core.model.Produto
import com.z1.comparaprecos.core.common.R
import com.z1.comparaprecos.core.model.ListaCompra
import com.z1.comparaprecos.feature.listaproduto.domain.ProdutoUseCase
import com.z1.comparaprecos.feature.listaproduto.presentation.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
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
import java.util.concurrent.TimeUnit
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
            is OnEvent.GetListaCompraToComparar -> getListaCompraToComparar(event.idListaCompra)
            is OnEvent.GetAllListaCompra -> getAllListaCompra()
            is OnEvent.InsertProduto -> insertProduto(event.produto)
            is OnEvent.UpdateProduto -> updateProduto(event.produto)
            is OnEvent.DeleteProduto -> deleteProduto(event.produto)
            is OnEvent.ProdutoSelecionado -> updateProdutoSelecionado(event.produto)
            is OnEvent.UpdateQuantidadeProdutoExistente -> updateProdutoJaExistente(event.produto)
            is OnEvent.UpdateUiEvent -> updateUiEvent(event.uiEvent)
        }
    }

    private fun updateUiEvent(uiEvent: UiEvent) =
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }

    private fun insertProduto(produto: Produto) = viewModelScope.launch {
        if (isProdutoJaExiste(produto) != null) {
            updateProdutoJaExiste(produto)
            _uiEvent.send(UiEvent.Error(UiText.StringResource(R.string.label_desc_produto_existente)))
        } else {
            val result = isDadosProdutoCorreto(produto)
            if (result.first) {
                val isProdutoAdiconado = produtoUseCase.insertProduto(produto)
                _uiEvent.send(if (isProdutoAdiconado > 0) UiEvent.Success else UiEvent.Default)
            } else {
                _uiEvent.send(UiEvent.ShowSnackbar(result.second!!))
            }
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

    private fun updateProdutoJaExistente(produto: Produto?) {
        produto?.let {
            val produtoNaLista = isProdutoJaExiste(produto)
            produtoNaLista?.let {
                val novaQuantidade  = atualizarNovaQuantidadeProduto(produto, produtoNaLista)
                val produtoAtualizado = produtoNaLista.copy(quantidade = novaQuantidade)
                updateProduto(produtoAtualizado)
            }
        } ?: updateProdutoJaExiste()
    }

    private fun atualizarNovaQuantidadeProduto(produto: Produto, produtoNaLista: Produto) =
        BigDecimal(produtoNaLista.quantidade).plus(BigDecimal(produto.quantidade)).toString()


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

    private fun updateProdutoJaExiste(produto: Produto? = null) {
        _uiState.update { currentState ->
            currentState.copy(
                produtoJaExiste = produto
            )
        }
        if (produto == null) updateUiEvent(UiEvent.Default)
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

    private fun isProdutoJaExiste(produto: Produto): Produto? =
        _uiState.value.listaProduto.find { it.nomeProduto == produto.nomeProduto }

    private fun updateListaCompra(listaCompra: ListaCompra) {
        _uiState.update { currentState ->
            currentState.copy(
                listaCompra = listaCompra
            )
        }
    }

    private fun updateAllListaCompra(allListaCompra: List<Pair<String, Long>>) {
        _uiState.update { currentState ->
            currentState.copy(
                allListaCompra = allListaCompra
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
            currentState.copy(
                isListaProdutoCarregada = true,
                listaProduto = listaProduto
            )
        }
    }

    private fun getAllListaCompra() =
        viewModelScope.launch {
            try {
                _uiState.value.allListaCompra.isEmpty().also {
                    val listaCompra = produtoUseCase.getAllListaCompra()
                    if (listaCompra.isNotEmpty()) {
                        val pairListaCompra = listaCompra.filter {
                            it.id != _uiState.value.listaCompra.id
                        }.map {
                            it.titulo to it.id
                        }
                        updateAllListaCompra(pairListaCompra)
                    }
                }

                if (_uiState.value.allListaCompra.isEmpty()) {
                    updateUiEvent(UiEvent.ShowSnackbar(UiText.StringResource(R.string.label_desc_erro_listas_to_comparar)))
                } else {
                    updateUiEvent(UiEvent.ShowAlertDialog)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                updateUiEvent(UiEvent.ShowSnackbar(UiText.StringResource(R.string.label_desc_erro_buscar_listas_compra)))
            }
        }

    private fun getListaCompra(idListaCompra: Long) =
        viewModelScope.launch {
            delay(TimeUnit.SECONDS.toMillis(2))
            val listaCompra = try {
                produtoUseCase.getListaCompra(idListaCompra)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }

            listaCompra?.let {
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

    private fun getListaCompraToComparar(idListaCompraComparada: Long) =
        viewModelScope.launch {
            val listaCompra = try {
                produtoUseCase.getListaCompraComparada(idListaCompraComparada)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }

            listaCompra?.let {
                updateListaCompraComparada(it)
                _uiEvent.send(UiEvent.Default)
            } ?: _uiEvent.send(
                UiEvent.Error(
                    UiText.StringResource(R.string.label_lista_compra_comparada_nao_encontrada)
                )
            )
        }
}