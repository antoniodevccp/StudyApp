package cl.almo.studyapp.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.almo.studyapp.ui.theme.Spacing

/**
 * Pantalla inicial: lista de conceptos agrupados por categoría. Toda la
 * tarjeta es pulsable (vía el overload `Card(onClick = ...)` de Material 3,
 * que ya trae el ripple y el objetivo táctil correctos) y abre su demo.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    onSelect: (ConceptId) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = { Text("Estudio Android") })
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = Spacing.sm),
            verticalArrangement = Arrangement.spacedBy(Spacing.sm),
            contentPadding = PaddingValues(vertical = Spacing.sm)
        ) {
            ConceptCategory.entries.forEach { category ->
                val itemsInCategory = concepts.filter { it.category == category }
                if (itemsInCategory.isEmpty()) return@forEach

                item(key = "header-${category.name}") {
                    Text(
                        text = category.label,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                items(items = itemsInCategory, key = { it.id }) { concept ->
                    ConceptCard(concept = concept, onClick = { onSelect(concept.id) })
                }
            }
        }
    }
}

@Composable
private fun ConceptCard(concept: ConceptItem, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(Spacing.sm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = concept.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = Spacing.sm)
            )
            Column {
                Text(text = concept.title, style = MaterialTheme.typography.titleLarge)
                Text(text = concept.summary, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
