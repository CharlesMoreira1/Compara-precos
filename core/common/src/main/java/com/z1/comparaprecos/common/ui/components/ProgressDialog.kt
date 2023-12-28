@file:OptIn(ExperimentalMaterial3Api::class)

package com.z1.comparaprecos.common.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.z1.comparaprecos.common.ui.theme.ComparaPrecosTheme
import com.z1.comparaprecos.core.common.R

@Composable
fun CustomProgressDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    @StringRes titulo: Int = R.string.label_desc_carregando_listas
) {

   Dialog(onDismissRequest = { onDismiss() }) {
       Card(
           modifier = modifier
               .fillMaxWidth(),
           elevation = CardDefaults.cardElevation(
               defaultElevation = dimensionResource(id = R.dimen.normal)
           ),
       ) {
           Column(
               modifier = Modifier
                   .fillMaxWidth()
                   .background(MaterialTheme.colorScheme.surface)
                   .padding(dimensionResource(id = R.dimen.medium)),
               horizontalAlignment = Alignment.CenterHorizontally
           ) {
               CircularProgressIndicator(
                   modifier = Modifier
                       .padding(dimensionResource(id = R.dimen.normal)),
                   strokeWidth = 4.dp
               )
               Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium)))
               Text(
                   text = stringResource(id = titulo),
                   textAlign = TextAlign.Center,
                   style = MaterialTheme.typography.headlineSmall
               )
               
           }
       }
   }
        
}

@Preview(showSystemUi = true)
@Composable
fun PreviewProgressDialog() {
    ComparaPrecosTheme {
        CustomProgressDialog(onDismiss = {})
    }
}