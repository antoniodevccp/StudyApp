package cl.almo.studyapp.catalog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cl.almo.studyapp.ui.theme.MinTouchTarget
import cl.almo.studyapp.ui.theme.Spacing

/**
 * Armazón común de cada demo: barra superior con el nombre del concepto y
 * un botón Atrás, el selector «Animales | Banca», una explicación breve, y
 * debajo el área del experimento. Centralizarlo acá evita repetir este
 * bloque en los 5 archivos de pantalla, y asegura que las cinco demos
 * respeten la misma jerarquía visual.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoScaffold(
    title: String,
    domain: Domain,
    onDomainChange: (Domain) -> Unit,
    explanation: String,
    onBack: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.heightIn(min = MinTouchTarget)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver al catálogo")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(Spacing.sm)
        ) {
            DomainSelector(domain = domain, onDomainChange = onDomainChange)
            Spacer(modifier = Modifier.height(Spacing.sm))
            Text(text = explanation, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(Spacing.lg))
            content()
        }
    }
}
