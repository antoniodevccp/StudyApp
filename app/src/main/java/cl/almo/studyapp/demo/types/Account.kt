package cl.almo.studyapp.demo.types

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Encapsula el comportamiento de una cuenta: cuánto saldo tiene y las
 * reglas de cómo retirar. Es una `class` (no una `data class`): no es un
 * paquete de datos comparable por valor, es ESTA cuenta, con un saldo que
 * solo cambia a través de [retirar] — `balance` tiene setter privado, nada
 * de afuera puede pisarlo directamente.
 *
 * Recibe un [MovementSource] por constructor en vez de crear uno fijo
 * adentro — así [changeSource] puede sustituirlo por cualquier otra
 * implementación de la interfaz sin que Account sepa ni le importe cuál
 * es.
 */
class Account(
    initialBalance: Int,
    initialSource: MovementSource
) {
    private var source: MovementSource = initialSource

    var balance: Int by mutableStateOf(initialBalance)
        private set

    var movements: List<Movement> by mutableStateOf(initialSource.loadMovements())
        private set

    val sourceLabel: String get() = source.label

    fun changeSource(newSource: MovementSource) {
        source = newSource
        movements = newSource.loadMovements()
    }

    fun retirar(monto: Int): WithdrawalResult {
        return if (monto > balance) {
            WithdrawalResult.SaldoInsuficiente(saldoActual = balance, montoSolicitado = monto)
        } else {
            balance -= monto
            WithdrawalResult.Aprobado(nuevoSaldo = balance)
        }
    }
}
