package com.z1.comparaprecos.common.ui.components

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.z1.comparaprecos.common.ui.theme.ComparaPrecosTheme
import com.z1.comparaprecos.common.ui.theme.CoralRed
import com.z1.comparaprecos.common.ui.theme.MediumSeaGreen
import com.z1.comparaprecos.common.ui.theme.OrangePeel
import com.z1.comparaprecos.core.common.R
import kotlinx.coroutines.delay
import java.time.Instant
import java.util.concurrent.TimeUnit

enum class ETipoSnackbar {
    SUCESSO,
    AVISO,
    ERRO
}

data class Mensagem(
    @StringRes val titulo: Int,
    val tipoMensagem: ETipoSnackbar,
    val id: Long = Instant.now().toEpochMilli()
)

@Composable
fun CustomSnackBar(
    modifier: Modifier = Modifier,
    mensagem: Mensagem,
    onFimShowMensagem: (Mensagem) -> Unit,
    icone: ImageVector = Icons.Rounded.CheckCircleOutline,
    iconeTint: Color = MaterialTheme.colorScheme.onPrimary,
    duracao: Long = TimeUnit.SECONDS.toMillis(5)
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = mensagem.id) {
        isVisible = true
        delay(duracao)
        isVisible = false
        delay(TimeUnit.SECONDS.toMillis(1))
        onFimShowMensagem(mensagem)
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.medium)),
            colors = CardDefaults.cardColors(
                containerColor = when(mensagem.tipoMensagem) {
                    ETipoSnackbar.SUCESSO -> MediumSeaGreen
                    ETipoSnackbar.AVISO -> OrangePeel
                    ETipoSnackbar.ERRO -> CoralRed
                }
            ),
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.big)),
            elevation = CardDefaults.cardElevation(
                defaultElevation = dimensionResource(id = R.dimen.normal)
            )
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.medium)),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(id = mensagem.titulo),
                    color = iconeTint,
                    style = MaterialTheme.typography.titleMedium
                )
                Icon(
                    imageVector = icone,
                    contentDescription = null,
                    tint = iconeTint
                )
            }
        }
    }
}

@Preview(showSystemUi = false, showBackground = false)
@Composable
private fun PreviewCustomSnackbar() {
    ComparaPrecosTheme {
        CustomSnackBar(
            mensagem = Mensagem(R.string.app_name, ETipoSnackbar.ERRO, Instant.now().toEpochMilli()),
            onFimShowMensagem = {}
        )
    }
}