package cl.almo.studyapp.catalog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cl.almo.studyapp.ui.theme.Spacing

/**
 * El registro técnico (línea por línea) de un experimento: ayuda a
 * entender el mecanismo, pero NO es lo primero que hay que mirar — por eso
 * usa un contenedor más chico y discreto que [StatusBadge].
 */
@Composable
fun TechnicalLogCard(
    title: String,
    lines: List<String>,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(Spacing.sm)) {
            Text(text = title, style = MaterialTheme.typography.labelLarge)
            lines.forEach { line ->
                Text(text = line, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
