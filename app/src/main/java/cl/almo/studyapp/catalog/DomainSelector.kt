package cl.almo.studyapp.catalog

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cl.almo.studyapp.ui.theme.MinTouchTarget
import cl.almo.studyapp.ui.theme.Spacing

/**
 * Selector «Animales | Banca» que aparece arriba del contenido de cada
 * demo. Cambiar de opción no reinicia la app: cada rama del `when` que lee
 * [Domain] en la pantalla que usa este selector es un subárbol de
 * composición distinto, así que Compose recuerda el estado de cada
 * temática por separado mientras no se salga de la demo.
 */
@Composable
fun DomainSelector(domain: Domain, onDomainChange: (Domain) -> Unit) {
    Row {
        Domain.entries.forEachIndexed { index, option ->
            if (index > 0) {
                Spacer(modifier = Modifier.width(Spacing.xs))
            }
            val icon = if (option == Domain.ANIMALS) Icons.Default.Pets else Icons.Default.AccountBalance
            if (option == domain) {
                Button(
                    onClick = { onDomainChange(option) },
                    modifier = Modifier.heightIn(min = MinTouchTarget)
                ) {
                    Icon(icon, contentDescription = null)
                    Spacer(modifier = Modifier.width(Spacing.xs))
                    Text(option.label)
                }
            } else {
                OutlinedButton(
                    onClick = { onDomainChange(option) },
                    modifier = Modifier.heightIn(min = MinTouchTarget)
                ) {
                    Icon(icon, contentDescription = null)
                    Spacer(modifier = Modifier.width(Spacing.xs))
                    Text(option.label)
                }
            }
        }
    }
}
