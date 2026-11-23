package cl.almo.studyapp.demo.structured

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay

/**
 * Simula una tarea larga (p.ej. sincronizar datos) que avanza en pasos y
 * reporta su progreso. Si la corrutina que la ejecuta se cancela —ya sea
 * por su propio Job o porque se canceló el scope entero—, el `delay`
 * lanza `CancellationException` en el paso en que estaba.
 */
suspend fun runLongTask(name: String, steps: Int = 6, onEvent: (String) -> Unit) {
    onEvent("▶ $name: iniciada")
    try {
        repeat(steps) { i ->
            delay(600)
            onEvent("… $name: paso ${i + 1}/$steps")
        }
        onEvent("✔ $name: terminada normalmente")
    } catch (e: CancellationException) {
        onEvent("⏹ $name: cancelada")
        throw e // repropagar siempre: cancelar sin relanzar rompe la cooperación de corrutinas
    }
}
