package com.z1.comparaprecos.feature.listaproduto.presentation.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.z1.comparaprecos.common.ui.components.ETipoSnackbar
import com.z1.comparaprecos.common.util.UiText
import com.z1.comparaprecos.core.common.R
import com.z1.comparaprecos.core.model.ListaCompra
import com.z1.comparaprecos.core.model.ListaCompraWithProdutos
import com.z1.comparaprecos.core.model.Produto
import com.z1.comparaprecos.core.model.exceptions.ErrorProductData
import com.z1.comparaprecos.core.model.exceptions.ErrorProductExists
import com.z1.comparaprecos.feature.listaproduto.domain.ProdutoUseCase
import com.z1.comparaprecos.feature.listaproduto.presentation.UiEvent
import com.z1.comparaprecos.feature.listaproduto.presentation.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class ProdutoViewModel @Inject constructor(
    private val produtoUseCase: ProdutoUseCase
) : ViewModel() {

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
            is OnEvent.GetListaCompraOptions -> getListaCompraOptions()
            is OnEvent.InsertProduto -> insertProduto(event.produto)
            is OnEvent.UpdateProduto -> updateProduto(event.produto)
            is OnEvent.DeleteProduto -> deleteProduto(event.produto)
            is OnEvent.ProdutoSelecionado -> updateProdutoSelecionado(event.produto)
            is OnEvent.UpdateQuantidadeProdutoExistente -> updateProdutoExistente(event.produto)
            is OnEvent.UpdateUiEvent -> updateUiEvent(event.uiEvent)
        }
    }

    //DATABASE

    //Insert
    private fun insertProduto(produto: Produto) =
        viewModelScope.launch {
            try {
                val listaProduto = _uiState.value.listaProduto
                produtoUseCase.insertProduto(produto, listaProduto)
                _uiEvent.send(UiEvent.Success)
            } catch (e: ErrorProductExists) {
                e.printStackTrace()
                updateProdutoJaExiste(produto)
                _uiEvent.send(
                    UiEvent.Error(
                        UiText.StringResource(
                            resId = R.string.label_desc_produto_existente,
                            produto.nomeProduto,
                            produto.quantidade
                        )
                    )
                )
            } catch (e: ErrorProductData) {
                e.printStackTrace()
                _uiEvent.send(
                    UiEvent.ShowSnackbar(
                        UiText.StringResource(
                            e.getUiMessageId() ?: R.string.label_erro_generico
                        ),
                        ETipoSnackbar.ERRO
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
                _uiEvent.send(
                    UiEvent.ShowSnackbar(
                        UiText.StringResource(
                            R.string.label_erro_generico
                        ),
                        ETipoSnackbar.ERRO
                    )
                )
            }
        }

    //Get
    private fun getListaCompra(idListaCompra: Long) =
        viewModelScope.launch {
            val listaCompra = try {
                produtoUseCase.getListaCompra(idListaCompra)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }

            listaCompra?.let {
                setListaCompra(it)
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
                .catch {
                    _uiEvent.send(UiEvent.Error(UiText.DynamicString(it.message ?: "")))
                }
                .collect {
                    setListaProduto(it)
                }
        }

    private fun getListaCompraOptions() =
        viewModelScope.launch {
            try {
                if (_uiState.value.listaCompraOptions.isEmpty()) {
                    val listaCompraOpcoes =
                        produtoUseCase.getListaCompraOptions(_uiState.value.listaCompra.id)
                    setAllListaCompra(listaCompraOpcoes)
                }
                updateUiEvent(UiEvent.ShowAlertDialog)

            } catch (e: Exception) {
                e.printStackTrace()
                updateUiEvent(
                    UiEvent.ShowSnackbar(
                        UiText.StringResource(R.string.label_desc_erro_buscar_listas_compra),
                        ETipoSnackbar.SUCESSO
                    )
                )
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

    //Update
    private fun updateProduto(produto: Produto) =
        viewModelScope.launch {
            try {
                val resultMessage = produtoUseCase.updateProduto(produto)
                updateProdutoSelecionado()
                _uiEvent.send(
                    UiEvent.ShowSnackbar(
                        UiText.StringResource(resultMessage),
                        ETipoSnackbar.SUCESSO
                    )
                )
            } catch (e: Exception) {
                _uiEvent.send(UiEvent.Error(UiText.StringResource(R.string.label_desc_erro_editar_produto)))
            }
        }

    private fun updateProdutoExistente(produto: Produto?) {
        produto?.let {
            val produtoNaLista = findProduto(produto)
            produtoNaLista?.let {
                val novaQuantidade = novaQuantidadeProduto(produto, produtoNaLista)
                val produtoAtualizado = produtoNaLista.copy(quantidade = novaQuantidade)
                updateProduto(produtoAtualizado)
            }
        } ?: updateProdutoJaExiste()
    }

    private fun novaQuantidadeProduto(produto: Produto, produtoNaLista: Produto) =
        BigDecimal(produtoNaLista.quantidade).plus(BigDecimal(produto.quantidade)).toString()

    private fun findProduto(produto: Produto): Produto? =
        _uiState.value.listaProduto.find { it.nomeProduto == produto.nomeProduto }

    //Delete
    private fun deleteProduto(produto: Produto) =
        viewModelScope.launch {
            try {
                val resultMessage = produtoUseCase.deleteProduto(produto)
                _uiEvent.send(
                    UiEvent.ShowSnackbar(
                        UiText.StringResource(resultMessage),
                        ETipoSnackbar.SUCESSO
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
                _uiEvent.send(
                    UiEvent.ShowSnackbar(
                        UiText.StringResource(R.string.label_desc_erro_excluir_produto),
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

    private fun setListaCompra(listaCompra: ListaCompra) {
        _uiState.update { currentState ->
            currentState.copy(
                listaCompra = listaCompra
            )
        }
    }

    private fun setListaProduto(listaProduto: List<Produto>) {
        _uiState.update { currentState ->
            currentState.copy(
                isListaProdutoCarregada = true,
                listaProduto = listaProduto
            )
        }
    }

    private fun setAllListaCompra(allListaCompra: List<Pair<String, Long>>) {
        _uiState.update { currentState ->
            currentState.copy(
                listaCompraOptions = allListaCompra
            )
        }
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

    private fun updateListaCompraComparada(listaCompra: ListaCompraWithProdutos) {
        _uiState.update { currentState ->
            currentState.copy(listaCompraComparada = listaCompra)
        }
    }
    //UISTATE

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun collectUiEvent() = runBlocking { _uiEvent.receive() }
}