package cl.almo.studyapp.demo.flow

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cl.almo.studyapp.catalog.DemoScaffold
import cl.almo.studyapp.catalog.Domain
import cl.almo.studyapp.catalog.StatusBadge
import cl.almo.studyapp.catalog.StatusTone
import cl.almo.studyapp.catalog.TechnicalLogCard
import cl.almo.studyapp.ui.theme.MinTouchTarget
import cl.almo.studyapp.ui.theme.Spacing
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Demo de Flow: separa visualmente lo que emite el productor de lo que
 * recibe el colector, y deja ver que un Flow frío vuelve a correr su
 * bloque entero cada vez que se lo colecciona de nuevo.
 */
@Composable
fun FlowDemoScreen(onBack: () -> Unit) {
    var domain by remember { mutableStateOf(Domain.ANIMALS) }

    DemoScaffold(
        title = "Flow",
        domain = domain,
        onDomainChange = { domain = it },
        explanation = "Un Flow frío describe una secuencia de emisión; no hace nada hasta que " +
            "una corrutina lo colecciona con collect(), y esa corrutina es la que se suspende " +
            "en cada delay del productor — el Flow en sí no \"corre\" solo. Cada colección " +
            "nueva vuelve a ejecutar la receta desde el principio.",
        onBack = onBack
    ) {
        when (domain) {
            Domain.ANIMALS -> AnimalsFlowContent()
            Domain.BANKING -> BankingFlowContent()
        }
    }
}

/** La cámara del bosque emite; el observador colecciona. */
@Composable
private fun AnimalsFlowContent() {
    val scope = rememberCoroutineScope()

    var job by remember { mutableStateOf<Job?>(null) }
    var isObserving by remember { mutableStateOf(false) }
    var attempt by remember { mutableStateOf(0) }
    var cameraLog by remember { mutableStateOf(listOf<String>()) }
    var observerLog by remember { mutableStateOf(listOf<String>()) }

    fun startObserving() {
        if (isObserving) return
        isObserving = true
        attempt++
        cameraLog = cameraLog + "— Intento #$attempt: la cámara arranca de cero —"
        observerLog = observerLog + "— Intento #$attempt —"
        job = scope.launch {
            forestCameraFlow(onEmit = { event -> cameraLog = cameraLog + event })
                .collect { animal ->
                    observerLog = observerLog + "👀 Observador recibe a $animal"
                }
            observerLog = observerLog + "Observación terminada"
            isObserving = false
        }
    }

    fun stopObserving() {
        job?.cancel()
        isObserving = false
        observerLog = observerLog + "⏹ Observación detenida manualmente"
    }

    val status = if (isObserving) "Observando" else if (attempt == 0) "Sin iniciar" else "Detenido"
    val tone = if (isObserving) StatusTone.ACTIVE else if (attempt == 0) StatusTone.NEUTRAL else StatusTone.SUCCESS
    val icon = if (isObserving) Icons.Default.PhotoCamera else Icons.Default.Pets

    StatusBadge(text = status, tone = tone, icon = icon)
    Spacer(modifier = Modifier.height(Spacing.sm))
    Row {
        Button(
            onClick = ::startObserving,
            enabled = !isObserving,
            modifier = Modifier.heightIn(min = MinTouchTarget)
        ) { Text("Observar") }
        Spacer(modifier = Modifier.width(Spacing.xs))
        OutlinedButton(
            onClick = ::stopObserving,
            enabled = isObserving,
            modifier = Modifier.heightIn(min = MinTouchTarget)
        ) { Text("Dejar de observar") }
    }
    Spacer(modifier = Modifier.height(Spacing.lg))
    Row {
        TechnicalLogCard(
            title = "Cámara (flow { ... })",
            lines = cameraLog,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(Spacing.xs))
        TechnicalLogCard(
            title = "Observador (collect { ... })",
            lines = observerLog,
            modifier = Modifier.weight(1f)
        )
    }
}

/** El productor bancario emite movimientos; el colector los recibe. */
@Composable
private fun BankingFlowContent() {
    val scope = rememberCoroutineScope()

    var job by remember { mutableStateOf<Job?>(null) }
    var isCollecting by remember { mutableStateOf(false) }
    var attempt by remember { mutableStateOf(0) }
    var producerLog by remember { mutableStateOf(listOf<String>()) }
    var collectorLog by remember { mutableStateOf(listOf<String>()) }

    fun startCollecting() {
        if (isCollecting) return
        isCollecting = true
        attempt++
        producerLog = producerLog + "— Intento #$attempt: el productor arranca de cero —"
        collectorLog = collectorLog + "— Intento #$attempt —"
        job = scope.launch {
            accountMovementsFlow(onEmit = { event -> producerLog = producerLog + event })
                .collect { movimiento ->
                    collectorLog = collectorLog + "🧾 Colector recibe \"$movimiento\""
                }
            collectorLog = collectorLog + "Colección terminada"
            isCollecting = false
        }
    }

    fun stopCollecting() {
        job?.cancel()
        isCollecting = false
        collectorLog = collectorLog + "⏹ Colección detenida manualmente"
    }

    val status = if (isCollecting) "Coleccionando" else if (attempt == 0) "Sin iniciar" else "Detenido"
    val tone = if (isCollecting) StatusTone.ACTIVE else if (attempt == 0) StatusTone.NEUTRAL else StatusTone.SUCCESS
    val icon = if (isCollecting) Icons.Default.Sync else Icons.Default.AccountBalance

    StatusBadge(text = status, tone = tone, icon = icon)
    Spacer(modifier = Modifier.height(Spacing.sm))
    Row {
        Button(
            onClick = ::startCollecting,
            enabled = !isCollecting,
            modifier = Modifier.heightIn(min = MinTouchTarget)
        ) { Text("Iniciar colección") }
        Spacer(modifier = Modifier.width(Spacing.xs))
        OutlinedButton(
            onClick = ::stopCollecting,
            enabled = isCollecting,
            modifier = Modifier.heightIn(min = MinTouchTarget)
        ) { Text("Detener") }
    }
    Spacer(modifier = Modifier.height(Spacing.lg))
    Row {
        TechnicalLogCard(
            title = "Productor (flow { ... })",
            lines = producerLog,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(Spacing.xs))
        TechnicalLogCard(
            title = "Colector (collect { ... })",
            lines = collectorLog,
            modifier = Modifier.weight(1f)
        )
    }
}
