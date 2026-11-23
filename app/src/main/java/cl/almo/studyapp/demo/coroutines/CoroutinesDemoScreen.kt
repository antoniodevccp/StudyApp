package cl.almo.studyapp.demo.coroutines

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.FlightLand
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.almo.studyapp.catalog.DemoScaffold
import cl.almo.studyapp.catalog.Domain
import cl.almo.studyapp.catalog.StatusBadge
import cl.almo.studyapp.catalog.StatusTone
import cl.almo.studyapp.catalog.TechnicalLogCard
import cl.almo.studyapp.ui.theme.MinTouchTarget
import cl.almo.studyapp.ui.theme.Spacing
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Demo de corrutinas. El concepto es el mismo en las dos pestañas: una
 * operación tarda, se suspende en cada `delay` sin bloquear el hilo, y
 * algo independiente (dar una semilla / el contador suelto) sigue
 * respondiendo mientras tanto. Lo que está pasando AHORA ([StatusBadge] +
 * la barra de progreso) tiene prioridad visual sobre el registro técnico.
 */
@Composable
fun CoroutinesDemoScreen(onBack: () -> Unit) {
    var domain by remember { mutableStateOf(Domain.ANIMALS) }

    DemoScaffold(
        title = "Corrutinas",
        domain = domain,
        onDomainChange = { domain = it },
        explanation = "Una corrutina puede correr en el hilo principal sin bloquearlo: cuando " +
            "llega a un delay (o cualquier otra función suspend), se suspende — libera el hilo " +
            "en vez de ocuparlo esperando — y lo retoma cuando el tiempo pasó.",
        onBack = onBack
    ) {
        when (domain) {
            Domain.ANIMALS -> AnimalsCoroutinesContent()
            Domain.BANKING -> BankingCoroutinesContent()
        }
    }
}

/**
 * Un pájaro vuela 10 segundos. [BirdFlightSimulator.fly] marca con
 * comentarios dónde se suspende y dónde se reanuda la corrutina.
 */
@Composable
private fun AnimalsCoroutinesContent() {
    val scope = rememberCoroutineScope()
    val simulator = remember { BirdFlightSimulator() }

    var job by remember { mutableStateOf<Job?>(null) }
    var status by remember { mutableStateOf("En tierra") }
    var elapsed by remember { mutableStateOf(0) }
    var seedCount by remember { mutableStateOf(0) }

    fun startFlight() {
        if (status == "Volando") return
        status = "Volando"
        elapsed = 0
        job = scope.launch {
            try {
                simulator.fly(durationSeconds = 10) { second -> elapsed = second }
                status = "Aterrizó"
            } catch (e: CancellationException) {
                status = "Vuelo cancelado"
                throw e // nunca se traga una CancellationException
            }
        }
    }

    fun cancelFlight() {
        job?.cancel()
        status = "Vuelo cancelado"
    }

    val tone = when (status) {
        "Volando" -> StatusTone.ACTIVE
        "Aterrizó" -> StatusTone.SUCCESS
        "Vuelo cancelado" -> StatusTone.PROBLEM
        else -> StatusTone.NEUTRAL
    }
    val icon = when (status) {
        "Volando" -> Icons.Default.Flight
        "Aterrizó" -> Icons.Default.Check
        "Vuelo cancelado" -> Icons.Default.Close
        else -> Icons.Default.FlightLand
    }

    // Lo más importante primero: el estado del pájaro y el progreso.
    StatusBadge(text = status, tone = tone, icon = icon)
    Spacer(modifier = Modifier.height(Spacing.xs))
    LinearProgressIndicator(
        progress = { elapsed / 10f },
        modifier = Modifier.fillMaxWidth().heightIn(min = 8.dp)
    )
    Text("Segundo $elapsed de 10", style = MaterialTheme.typography.bodyMedium)
    Spacer(modifier = Modifier.height(Spacing.sm))
    Row {
        Button(
            onClick = ::startFlight,
            enabled = status != "Volando",
            modifier = Modifier.heightIn(min = MinTouchTarget)
        ) { Text("Iniciar vuelo") }
        Spacer(modifier = Modifier.width(Spacing.xs))
        OutlinedButton(
            onClick = ::cancelFlight,
            enabled = status == "Volando",
            modifier = Modifier.heightIn(min = MinTouchTarget)
        ) { Text("Cancelar vuelo") }
    }
    Spacer(modifier = Modifier.height(Spacing.sm))
    Button(
        onClick = { seedCount++ },
        modifier = Modifier.heightIn(min = MinTouchTarget)
    ) { Text("Dar una semilla ($seedCount)") }

    // El registro técnico queda después, más chico: ayuda a entender el
    // mecanismo pero no es lo primero que hay que mirar.
    Spacer(modifier = Modifier.height(Spacing.lg))
    TechnicalLogCard(
        title = "Dónde se suspende y se reanuda",
        lines = listOf(
            "Cada delay(1000) en BirdFlightSimulator.fly() suspende la corrutina.",
            "onTick(second), justo después, es la reanudación — un segundo más tarde."
        )
    )
}

/**
 * "Cargar movimientos" tarda, como una llamada de red real. Mientras
 * tarda, el contador independiente de más abajo debería seguir
 * respondiendo sin trabas.
 */
@Composable
private fun BankingCoroutinesContent() {
    val scope = rememberCoroutineScope()
    val loader = remember { AccountMovementsLoader() }

    var currentJob by remember { mutableStateOf<Job?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var log by remember { mutableStateOf(listOf<String>()) }
    var independentCounter by remember { mutableStateOf(0) }

    fun start() {
        if (isLoading) return
        isLoading = true
        log = listOf("— Carga iniciada —")
        currentJob = scope.launch {
            val movimientos = loader.load(onEvent = { event -> log = log + event })
            log = log + "Total cargado: ${movimientos.size} movimientos"
            isLoading = false
        }
    }

    fun cancel() {
        currentJob?.cancel()
        isLoading = false
        log = log + "— Cancelada manualmente desde la UI —"
    }

    val status = when {
        isLoading -> "Cargando"
        log.isEmpty() -> "Sin iniciar"
        else -> "Completado"
    }
    val tone = when {
        isLoading -> StatusTone.ACTIVE
        log.isEmpty() -> StatusTone.NEUTRAL
        else -> StatusTone.SUCCESS
    }
    val icon = when {
        isLoading -> Icons.Default.Sync
        log.isEmpty() -> Icons.Default.AccountBalance
        else -> Icons.Default.Check
    }

    StatusBadge(text = status, tone = tone, icon = icon)
    Spacer(modifier = Modifier.height(Spacing.sm))
    Row {
        Button(
            onClick = ::start,
            enabled = !isLoading,
            modifier = Modifier.heightIn(min = MinTouchTarget)
        ) { Text("Cargar movimientos") }
        Spacer(modifier = Modifier.width(Spacing.xs))
        OutlinedButton(
            onClick = ::cancel,
            enabled = isLoading,
            modifier = Modifier.heightIn(min = MinTouchTarget)
        ) { Text("Cancelar") }
    }
    Spacer(modifier = Modifier.height(Spacing.sm))
    Text(
        text = "Contador independiente — tocalo mientras carga: $independentCounter",
        style = MaterialTheme.typography.titleMedium
    )
    Button(
        onClick = { independentCounter++ },
        modifier = Modifier.heightIn(min = MinTouchTarget)
    ) { Text("+1") }
    Spacer(modifier = Modifier.height(Spacing.lg))
    TechnicalLogCard(title = "Registro:", lines = log)
}
