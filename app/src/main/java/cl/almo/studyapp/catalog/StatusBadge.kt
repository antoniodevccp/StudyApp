package cl.almo.studyapp.catalog

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import cl.almo.studyapp.ui.theme.MinTouchTarget
import cl.almo.studyapp.ui.theme.Spacing

/** Qué tan "importante" es un estado, mapeado a roles de color de Material 3. */
enum class StatusTone { NEUTRAL, ACTIVE, SUCCESS, PROBLEM }

/**
 * Tarjeta grande: lo que está pasando AHORA en el experimento, con
 * prioridad visual sobre cualquier registro técnico. La reutilizan las
 * demos de corrutinas, Flow, Job/CoroutineScope, ciclo de vida y tipos —
 * cada una le pasa su propio texto, ícono y [StatusTone].
 */
@Composable
fun StatusBadge(
    text: String,
    tone: StatusTone,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    val containerColor = when (tone) {
        StatusTone.NEUTRAL -> MaterialTheme.colorScheme.surfaceVariant
        StatusTone.ACTIVE -> MaterialTheme.colorScheme.primaryContainer
        StatusTone.SUCCESS -> MaterialTheme.colorScheme.tertiaryContainer
        StatusTone.PROBLEM -> MaterialTheme.colorScheme.errorContainer
    }
    val contentColor = when (tone) {
        StatusTone.NEUTRAL -> MaterialTheme.colorScheme.onSurfaceVariant
        StatusTone.ACTIVE -> MaterialTheme.colorScheme.onPrimaryContainer
        StatusTone.SUCCESS -> MaterialTheme.colorScheme.onTertiaryContainer
        StatusTone.PROBLEM -> MaterialTheme.colorScheme.onErrorContainer
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = containerColor,
        contentColor = contentColor,
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            modifier = Modifier
                .heightIn(min = MinTouchTarget)
                .padding(horizontal = Spacing.sm, vertical = Spacing.xs),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null)
            Spacer(modifier = Modifier.width(Spacing.xs))
            Text(text = text, style = MaterialTheme.typography.titleLarge)
        }
    }
}
