package com.z1.comparaprecos.common.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.z1.comparaprecos.core.common.R

@Composable
fun CustomDialogOpcoes(
    modifier: Modifier = Modifier,
    title: String,
    optionList: List<Pair<*, Long>>,
    atualSelectedOption: Long,
    onAtualSelectedOptionClick: (Long) -> Unit
) {
    val configuration = LocalConfiguration.current

    var selectedOption by remember {
        mutableLongStateOf(atualSelectedOption)
    }

    AlertDialog(
        modifier = modifier.widthIn(max = configuration.screenWidthDp.dp - 80.dp),
        onDismissRequest = { },
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            Column {
                CustomDivider()
                DialogOptions(
                    optionList = optionList,
                    selectedOption = selectedOption,
                    onAtualSelectedOptionClick = {
                        selectedOption = it
                    }
                )
                CustomDivider()
            }
        },
        confirmButton = {
            TextButton(onClick = { onAtualSelectedOptionClick(selectedOption) }
            ) {
                Text(
                    text = stringResource(id = R.string.label_ok),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        },
    )
}

@Composable
fun DialogOptions(
    modifier: Modifier = Modifier,
    optionList: List<Pair<*, Long>>,
    selectedOption: Long,
    onAtualSelectedOptionClick: (Long) -> Unit
) {

    LazyColumn(
        modifier = modifier
            .heightIn(min = 150.dp, max = 250.dp)
    ) {
        items(optionList) { item ->
            CustomRadioButton(
                item = item,
                selectedOption = selectedOption,
                onClickListener = {
                    onAtualSelectedOptionClick(it)
                }
            )
        }
    }
}