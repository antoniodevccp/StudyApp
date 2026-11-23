package cl.almo.studyapp.demo.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay

/**
 * Simula la carga de movimientos de una cuenta, como si viniera de una red
 * lenta. No sabe nada de Compose ni de hilos de UI: es una función
 * `suspend` común y corriente. Cualquier corrutina que la llame queda
 * suspendida en cada `delay`, pero el hilo donde corre esa corrutina queda
 * libre para seguir atendiendo otras cosas — por eso el contador
 * independiente de la pantalla sigue respondiendo mientras esta función
 * "espera".
 */
class AccountMovementsLoader {

    private val pasos = listOf(
        "Depósito de sueldo: +\$650.000",
        "Compra supermercado: -\$18.300",
        "Pago de arriendo: -\$280.000"
    )

    suspend fun load(onEvent: (String) -> Unit): List<String> {
        onEvent("▶ Corrutina iniciada en el hilo \"${Thread.currentThread().name}\"")
        val cargados = mutableListOf<String>()
        try {
            for ((index, paso) in pasos.withIndex()) {
                onEvent("⏸ Suspendida: esperando el paso ${index + 1}/${pasos.size} (delay 1200ms)")
                delay(1200) // acá se suspende la corrutina; el hilo queda libre mientras tanto
                onEvent("▶ Reanudada en el hilo \"${Thread.currentThread().name}\"")
                cargados += paso
                onEvent("  Recibido: $paso")
            }
            onEvent("✔ Corrutina terminada normalmente")
        } catch (e: CancellationException) {
            onEvent("⏹ Corrutina cancelada (se interrumpió en medio del delay)")
            throw e // una CancellationException siempre se repropaga, nunca se traga
        }
        return cargados
    }
}
