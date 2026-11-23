package cl.almo.studyapp.demo.structured

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import cl.almo.studyapp.catalog.DemoScaffold
import cl.almo.studyapp.catalog.Domain
import cl.almo.studyapp.catalog.StatusBadge
import cl.almo.studyapp.catalog.StatusTone
import cl.almo.studyapp.catalog.TechnicalLogCard
import cl.almo.studyapp.ui.theme.MinTouchTarget
import cl.almo.studyapp.ui.theme.Spacing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * Demo de Job y CoroutineScope: dos tareas hijas en el mismo scope, para
 * ver la diferencia entre cancelar una tarea puntual (su Job) y cancelar
 * el contenedor entero (el scope), que cancela a todas las que sigan
 * vivas. El mecanismo ([StructuredConcurrencyContent]) es el mismo en las
 * dos pestañas — solo cambian los nombres de las tareas y el ícono.
 */
@Composable
fun StructuredConcurrencyScreen(onBack: () -> Unit) {
    var domain by remember { mutableStateOf(Domain.ANIMALS) }

    DemoScaffold(
        title = "Job y CoroutineScope",
        domain = domain,
        onDomainChange = { domain = it },
        explanation = "Dos tareas corren como hijas del mismo CoroutineScope (con " +
            "SupervisorJob, para que cancelar una no cancele a la otra). Cancelar el Job de " +
            "una tarea puntual no toca a su hermana; cancelar el scope entero cancela a todas " +
            "las que sigan activas dentro de él — esa es la cancelación estructurada.",
        onBack = onBack
    ) {
        when (domain) {
            Domain.ANIMALS -> StructuredConcurrencyContent(
                description = "Dos pájaros vuelan al mismo tiempo, cada uno con su propio Job.",
                nameA = "Pájaro 1",
                nameB = "Pájaro 2",
                cancelFirstLabel = "Cancelar vuelo del primero",
                cancelAllLabel = "Cancelar todos los vuelos",
                icon = Icons.Default.Flight
            )
            Domain.BANKING -> StructuredConcurrencyContent(
                description = "Dos transferencias corren al mismo tiempo, cada una con su propio Job.",
                nameA = "Transferencia A",
                nameB = "Transferencia B",
                cancelFirstLabel = "Cancelar solo A",
                cancelAllLabel = "Cancelar scope completo",
                icon = Icons.Default.AccountBalance
            )
        }
    }
}

@Composable
private fun StructuredConcurrencyContent(
    description: String,
    nameA: String,
    nameB: String,
    cancelFirstLabel: String,
    cancelAllLabel: String,
    icon: ImageVector
) {
    var resetKey by remember { mutableStateOf(0) }

    // Un CoroutineScope propio, creado a mano — distinto del que da
    // rememberCoroutineScope(). SupervisorJob() hace que cancelar o que
    // falle una tarea hija no cancele automáticamente a sus hermanas; solo
    // cancelar el scope entero (o cada Job individual) las afecta.
    val scope = remember(resetKey) { CoroutineScope(SupervisorJob() + Dispatchers.Main) }
    var jobA by remember(resetKey) { mutableStateOf<Job?>(null) }
    var jobB by remember(resetKey) { mutableStateOf<Job?>(null) }
    var log by remember(resetKey) { mutableStateOf(listOf<String>()) }

    // Si este composable sale de la composición (volvés al catálogo, o
    // cambiás de pestaña) o si resetKey cambia (reiniciaste la demo), se
    // cancela el scope vigente hasta ese momento. Así no queda ninguna
    // corrutina viva de fondo.
    DisposableEffect(scope) {
        onDispose { scope.cancel() }
    }

    fun launchTasks() {
        log = listOf("— Iniciando $nameA y $nameB en el mismo scope —")
        jobA = scope.launch { runLongTask(nameA, onEvent = { log = log + it }) }
        jobB = scope.launch { runLongTask(nameB, onEvent = { log = log + it }) }
    }

    fun cancelTaskA() {
        jobA?.cancel()
        log = log + "— Cancelada solo $nameA, a través de su propio Job —"
    }

    fun cancelScope() {
        scope.cancel()
        log = log + "— Scope cancelado: cualquier tarea que seguía activa se cancela con él —"
    }

    Text(description, style = MaterialTheme.typography.bodyMedium)
    Spacer(modifier = Modifier.height(Spacing.sm))

    StatusBadge(text = "$nameA: ${jobStatusLabel(jobA)}", tone = jobTone(jobA), icon = icon)
    Spacer(modifier = Modifier.height(Spacing.xs))
    StatusBadge(text = "$nameB: ${jobStatusLabel(jobB)}", tone = jobTone(jobB), icon = icon)
    Spacer(modifier = Modifier.height(Spacing.sm))

    Button(
        onClick = ::launchTasks,
        modifier = Modifier.heightIn(min = MinTouchTarget)
    ) { Text("Iniciar $nameA y $nameB") }
    Spacer(modifier = Modifier.height(Spacing.xs))
    Row {
        OutlinedButton(
            onClick = ::cancelTaskA,
            modifier = Modifier.heightIn(min = MinTouchTarget)
        ) { Text(cancelFirstLabel) }
        Spacer(modifier = Modifier.width(Spacing.xs))
        OutlinedButton(
            onClick = ::cancelScope,
            modifier = Modifier.heightIn(min = MinTouchTarget)
        ) { Text(cancelAllLabel) }
    }
    Spacer(modifier = Modifier.height(Spacing.xs))
    TextButton(
        onClick = { resetKey++ },
        modifier = Modifier.heightIn(min = MinTouchTarget)
    ) { Text("Reiniciar demo (scope nuevo)") }

    Spacer(modifier = Modifier.height(Spacing.lg))
    TechnicalLogCard(title = "Registro:", lines = log)
}

private fun jobStatusLabel(job: Job?): String = when {
    job == null -> "sin iniciar"
    job.isCancelled -> "cancelada"
    job.isCompleted -> "terminada"
    job.isActive -> "activa"
    else -> "desconocida"
}

private fun jobTone(job: Job?): StatusTone = when {
    job == null -> StatusTone.NEUTRAL
    job.isCancelled -> StatusTone.PROBLEM
    job.isCompleted -> StatusTone.SUCCESS
    job.isActive -> StatusTone.ACTIVE
    else -> StatusTone.NEUTRAL
}
