package cl.almo.studyapp.demo.lifecycle

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cl.almo.studyapp.catalog.DemoScaffold
import cl.almo.studyapp.catalog.Domain
import cl.almo.studyapp.catalog.StatusBadge
import cl.almo.studyapp.catalog.StatusTone
import cl.almo.studyapp.catalog.TechnicalLogCard
import cl.almo.studyapp.ui.theme.MinTouchTarget
import cl.almo.studyapp.ui.theme.Spacing

/**
 * Demo del ciclo de vida de la composición — DISTINTO del ciclo de vida de
 * la Activity. Ocultar y mostrar el componente no llama a `onCreate` de
 * nuevo: la Activity nunca se destruye. Lo que cambia es si ese componente
 * está o no en el árbol de composición en este momento.
 */
@Composable
fun LifecycleDemoScreen(onBack: () -> Unit) {
    var domain by remember { mutableStateOf(Domain.ANIMALS) }

    DemoScaffold(
        title = "Ciclo de vida en Compose",
        domain = domain,
        onDomainChange = { domain = it },
        explanation = "Esto es distinto del ciclo de vida de la Activity: ocultar y mostrar un " +
            "componente no llama a onCreate otra vez, la Activity sigue viva todo el tiempo. Lo " +
            "que entra y sale es la posición de ese componente en el árbol de composición.",
        onBack = onBack
    ) {
        when (domain) {
            Domain.ANIMALS -> LifecycleContent(
                description = "Mostrá y ocultá un nido. Mientras esté visible, podés cambiar " +
                    "la temperatura que observa y sumarle huevos.",
                toggleShownLabel = "Ocultar nido",
                toggleHiddenLabel = "Mostrar nido",
                watchedActionLabel = "Cambiar temperatura",
                watchedTextPrefix = "Temperatura observada (viene de afuera)",
                hiddenMessage = "El nido está oculto: no existe en la composición ahora.",
                componentTitle = "Nido",
                localTextPrefix = "Huevos en el nido (remember)",
                localActionLabel = "+1 huevo"
            )
            Domain.BANKING -> LifecycleContent(
                description = "Mostrá y ocultá un panel de saldo. Mientras esté visible, " +
                    "podés actualizar el saldo que observa y sumar consultas.",
                toggleShownLabel = "Ocultar panel",
                toggleHiddenLabel = "Mostrar panel",
                watchedActionLabel = "Actualizar saldo",
                watchedTextPrefix = "Saldo observado (viene de afuera)",
                hiddenMessage = "El panel está oculto: no existe en la composición ahora.",
                componentTitle = "Panel de saldo",
                localTextPrefix = "Consultas realizadas (remember)",
                localActionLabel = "+1 consulta"
            )
        }
    }
}

@Composable
private fun LifecycleContent(
    description: String,
    toggleShownLabel: String,
    toggleHiddenLabel: String,
    watchedActionLabel: String,
    watchedTextPrefix: String,
    hiddenMessage: String,
    componentTitle: String,
    localTextPrefix: String,
    localActionLabel: String
) {
    var visible by remember { mutableStateOf(true) }
    var watchedValue by remember { mutableStateOf(0) }
    var eventLog by remember { mutableStateOf(listOf<String>()) }

    Text(description, style = MaterialTheme.typography.bodyMedium)
    Spacer(modifier = Modifier.height(Spacing.sm))

    StatusBadge(
        text = "$componentTitle: ${if (visible) "Visible" else "Oculto"}",
        tone = if (visible) StatusTone.ACTIVE else StatusTone.NEUTRAL,
        icon = if (visible) Icons.Default.Visibility else Icons.Default.VisibilityOff
    )
    Spacer(modifier = Modifier.height(Spacing.sm))

    Row {
        Button(
            onClick = { visible = !visible },
            modifier = Modifier.heightIn(min = MinTouchTarget)
        ) {
            Text(if (visible) toggleShownLabel else toggleHiddenLabel)
        }
    }
    Spacer(modifier = Modifier.height(Spacing.xs))
    Row {
        Button(
            onClick = { watchedValue++ },
            modifier = Modifier.heightIn(min = MinTouchTarget)
        ) {
            Text("$watchedActionLabel ($watchedValue)")
        }
    }
    Spacer(modifier = Modifier.height(Spacing.sm))

    if (visible) {
        WatchedCard(
            componentTitle = componentTitle,
            watchedValue = watchedValue,
            watchedTextPrefix = watchedTextPrefix,
            localTextPrefix = localTextPrefix,
            localActionLabel = localActionLabel,
            onEvent = { eventLog = eventLog + it }
        )
    } else {
        Text(hiddenMessage, style = MaterialTheme.typography.bodyMedium)
    }

    Spacer(modifier = Modifier.height(Spacing.lg))
    TechnicalLogCard(title = "Registro de ciclo de vida:", lines = eventLog)
}

@Composable
private fun WatchedCard(
    componentTitle: String,
    watchedValue: Int,
    watchedTextPrefix: String,
    localTextPrefix: String,
    localActionLabel: String,
    onEvent: (String) -> Unit
) {
    // Este contador vive solo mientras ESTA instancia de WatchedCard sigue
    // en la composición. Si el padre deja de llamarla (visible = false),
    // Compose la olvida por completo: la próxima vez que vuelva a aparecer
    // es una instancia nueva, y localCount vuelve a arrancar en 0.
    var localCount by remember { mutableStateOf(0) }

    DisposableEffect(Unit) {
        onEvent("🟢 $componentTitle entra en composición (localCount arranca en 0)")
        onDispose {
            onEvent("🔴 $componentTitle sale de composición (se olvida localCount=$localCount)")
        }
    }

    // SideEffect corre después de CADA composición o recomposición que
    // termine bien — a diferencia de DisposableEffect, no tiene keys: se
    // ejecuta siempre que este composable se vuelve a dibujar con éxito.
    SideEffect {
        onEvent("✅ $componentTitle: composición aplicada (watchedValue=$watchedValue, localCount=$localCount)")
    }

    Card(modifier = Modifier.padding(vertical = Spacing.xs / 2)) {
        Column(modifier = Modifier.padding(Spacing.sm)) {
            Text(componentTitle, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(Spacing.xs))
            Text("$watchedTextPrefix: $watchedValue", style = MaterialTheme.typography.bodyLarge)
            Text("$localTextPrefix: $localCount", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(Spacing.xs))
            Button(
                onClick = { localCount++ },
                modifier = Modifier.heightIn(min = MinTouchTarget)
            ) { Text(localActionLabel) }
        }
    }
}
