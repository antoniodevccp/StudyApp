package cl.almo.studyapp.demo.types

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
import androidx.compose.runtime.Composable
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
 * Demo de tipos de Kotlin. El mismo patrón se repite en las dos pestañas:
 * una `class` que encapsula comportamiento ([Bird] / [Account]), una
 * `data class` comparable por valor ([Sighting] / [Movement]), una
 * `interface` con dos fuentes intercambiables ([SightingSource] /
 * [MovementSource]) y un `sealed interface` con el resultado de una
 * acción ([FlightResult] / [WithdrawalResult]).
 */
@Composable
fun TypesDemoScreen(onBack: () -> Unit) {
    var domain by remember { mutableStateOf(Domain.ANIMALS) }

    DemoScaffold(
        title = "Tipos de Kotlin",
        domain = domain,
        onDomainChange = { domain = it },
        explanation = "class, data class, interface y sealed interface trabajando juntos en " +
            "un caso real: una entidad con comportamiento, un dato comparable por valor, una " +
            "fuente intercambiable y un resultado cerrado que un when puede cubrir sin else.",
        onBack = onBack
    ) {
        when (domain) {
            Domain.ANIMALS -> AnimalsTypesContent()
            Domain.BANKING -> BankingTypesContent()
        }
    }
}

/** Bird (class) + Sighting (data class) + SightingSource (interface) + FlightResult (sealed). */
@Composable
private fun AnimalsTypesContent() {
    val bird = remember { Bird(initialEnergy = 100, initialSource = ForestSightingSource()) }
    var comparisonLog by remember { mutableStateOf(listOf<String>()) }
    var flightLog by remember { mutableStateOf(listOf<String>()) }
    var lastResult by remember { mutableStateOf("Sin intentos de vuelo todavía" to StatusTone.NEUTRAL) }

    fun compareSightings() {
        val original = bird.sightings.firstOrNull() ?: return
        val copiaIdentica = original.copy()
        val copiaModificada = original.copy(location = original.location + " (otro punto)")
        comparisonLog = listOf(
            "original          = $original",
            "copiaIdentica     = $copiaIdentica",
            "original == copiaIdentica   -> ${original == copiaIdentica}",
            "copiaModificada   = $copiaModificada",
            "original == copiaModificada -> ${original == copiaModificada}"
        )
    }

    fun tryFly(energyCost: Int) {
        // when exhaustivo sobre un sealed interface: no hace falta else,
        // el compilador sabe que FlightResult solo puede ser una de estas
        // dos variantes.
        val message = when (val result = bird.fly(energyCost)) {
            is FlightResult.Exitoso -> {
                lastResult = "Energía restante: ${result.energiaRestante}" to StatusTone.SUCCESS
                "✔ Vuelo de $energyCost energía exitoso. Energía restante: ${result.energiaRestante}"
            }
            is FlightResult.EnergiaInsuficiente -> {
                lastResult = "Energía insuficiente (${result.energiaActual} < ${result.energiaRequerida})" to StatusTone.PROBLEM
                "✘ Vuelo de ${result.energiaRequerida} rechazado: energía actual ${result.energiaActual}"
            }
        }
        flightLog = flightLog + message
    }

    StatusBadge(text = lastResult.first, tone = lastResult.second, icon = Icons.Default.Flight)
    Spacer(modifier = Modifier.height(Spacing.sm))
    Text("Energía actual: ${bird.energy}", style = MaterialTheme.typography.titleMedium)
    Text("Fuente actual: ${bird.sourceLabel}", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(Spacing.xs))
    Row {
        OutlinedButton(
            onClick = { bird.changeSource(ForestSightingSource()) },
            modifier = Modifier.heightIn(min = MinTouchTarget)
        ) { Text("Usar bosque") }
        Spacer(modifier = Modifier.width(Spacing.xs))
        OutlinedButton(
            onClick = { bird.changeSource(WetlandSightingSource()) },
            modifier = Modifier.heightIn(min = MinTouchTarget)
        ) { Text("Usar humedal") }
    }
    Spacer(modifier = Modifier.height(Spacing.sm))
    Text("Avistamientos (interface SightingSource):", style = MaterialTheme.typography.titleSmall)
    bird.sightings.forEach { sighting ->
        Text("· ${sighting.animal} en ${sighting.location}")
    }

    Spacer(modifier = Modifier.height(Spacing.lg))
    Text("data class: == y copy()", style = MaterialTheme.typography.titleSmall)
    Spacer(modifier = Modifier.height(Spacing.xs))
    Button(
        onClick = ::compareSightings,
        modifier = Modifier.heightIn(min = MinTouchTarget)
    ) { Text("Comparar con copy()") }
    Spacer(modifier = Modifier.height(Spacing.xs))
    TechnicalLogCard(title = "Comparaciones", lines = comparisonLog)

    Spacer(modifier = Modifier.height(Spacing.lg))
    Text("sealed interface: resultado de volar", style = MaterialTheme.typography.titleSmall)
    Spacer(modifier = Modifier.height(Spacing.xs))
    Row {
        Button(
            onClick = { tryFly(30) },
            modifier = Modifier.heightIn(min = MinTouchTarget)
        ) { Text("Volar (30 energía)") }
        Spacer(modifier = Modifier.width(Spacing.xs))
        OutlinedButton(
            onClick = { tryFly(500) },
            modifier = Modifier.heightIn(min = MinTouchTarget)
        ) { Text("Volar (500 energía)") }
    }
    Spacer(modifier = Modifier.height(Spacing.xs))
    TechnicalLogCard(title = "Intentos de vuelo", lines = flightLog)
}

/** Account (class) + Movement (data class) + MovementSource (interface) + WithdrawalResult (sealed). */
@Composable
private fun BankingTypesContent() {
    val account = remember { Account(initialBalance = 200_000, initialSource = CheckingAccountSource()) }
    var comparisonLog by remember { mutableStateOf(listOf<String>()) }
    var withdrawalLog by remember { mutableStateOf(listOf<String>()) }
    var lastResult by remember { mutableStateOf("Sin intentos de retiro todavía" to StatusTone.NEUTRAL) }

    fun compareMovements() {
        val original = account.movements.firstOrNull() ?: return
        val copiaIdentica = original.copy()
        val copiaModificada = original.copy(amount = original.amount + 1000)
        comparisonLog = listOf(
            "original          = $original",
            "copiaIdentica     = $copiaIdentica",
            "original == copiaIdentica   -> ${original == copiaIdentica}",
            "copiaModificada   = $copiaModificada",
            "original == copiaModificada -> ${original == copiaModificada}"
        )
    }

    fun withdraw(amount: Int) {
        // when exhaustivo sobre un sealed interface: no hace falta else,
        // el compilador sabe que WithdrawalResult solo puede ser una de
        // estas dos variantes.
        val message = when (val result = account.retirar(amount)) {
            is WithdrawalResult.Aprobado -> {
                lastResult = "Nuevo saldo: \$${result.nuevoSaldo}" to StatusTone.SUCCESS
                "✔ Retiro de \$$amount aprobado. Nuevo saldo: \$${result.nuevoSaldo}"
            }
            is WithdrawalResult.SaldoInsuficiente -> {
                lastResult = "Saldo insuficiente (\$${result.saldoActual})" to StatusTone.PROBLEM
                "✘ Retiro de \$${result.montoSolicitado} rechazado: saldo actual \$${result.saldoActual}"
            }
        }
        withdrawalLog = withdrawalLog + message
    }

    StatusBadge(text = lastResult.first, tone = lastResult.second, icon = Icons.Default.AccountBalance)
    Spacer(modifier = Modifier.height(Spacing.sm))
    Text("Fuente actual: ${account.sourceLabel}", style = MaterialTheme.typography.titleMedium)
    Text("Saldo: \$${account.balance}", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(Spacing.xs))
    Row {
        OutlinedButton(
            onClick = { account.changeSource(CheckingAccountSource()) },
            modifier = Modifier.heightIn(min = MinTouchTarget)
        ) { Text("Usar cuenta corriente") }
        Spacer(modifier = Modifier.width(Spacing.xs))
        OutlinedButton(
            onClick = { account.changeSource(SavingsAccountSource()) },
            modifier = Modifier.heightIn(min = MinTouchTarget)
        ) { Text("Usar cuenta de ahorro") }
    }
    Spacer(modifier = Modifier.height(Spacing.sm))
    Text("Movimientos (interface MovementSource):", style = MaterialTheme.typography.titleSmall)
    account.movements.forEach { movement ->
        Text("· ${movement.description}: ${movement.amount}")
    }

    Spacer(modifier = Modifier.height(Spacing.lg))
    Text("data class: == y copy()", style = MaterialTheme.typography.titleSmall)
    Spacer(modifier = Modifier.height(Spacing.xs))
    Button(
        onClick = ::compareMovements,
        modifier = Modifier.heightIn(min = MinTouchTarget)
    ) { Text("Comparar con copy()") }
    Spacer(modifier = Modifier.height(Spacing.xs))
    TechnicalLogCard(title = "Comparaciones", lines = comparisonLog)

    Spacer(modifier = Modifier.height(Spacing.lg))
    Text("sealed interface: resultado del retiro", style = MaterialTheme.typography.titleSmall)
    Spacer(modifier = Modifier.height(Spacing.xs))
    Row {
        Button(
            onClick = { withdraw(10000) },
            modifier = Modifier.heightIn(min = MinTouchTarget)
        ) { Text("Retirar \$10.000") }
        Spacer(modifier = Modifier.width(Spacing.xs))
        OutlinedButton(
            onClick = { withdraw(999000) },
            modifier = Modifier.heightIn(min = MinTouchTarget)
        ) { Text("Retirar \$999.000") }
    }
    Spacer(modifier = Modifier.height(Spacing.xs))
    TechnicalLogCard(title = "Intentos de retiro", lines = withdrawalLog)
}
