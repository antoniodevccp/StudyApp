package cl.almo.studyapp.demo.coroutines

import kotlinx.coroutines.delay

/**
 * Simula el vuelo de un pájaro: avanza segundo a segundo durante
 * [durationSeconds]. Cada `delay(1000)` es un punto de SUSPENSIÓN: la
 * corrutina se detiene justo ahí y el hilo queda libre. Cuando el segundo
 * transcurre, la corrutina se REANUDA desde esa misma línea y sigue con
 * `onTick(second)`. Si la corrutina que llama a [fly] se cancela, el
 * `delay` es quien lanza la `CancellationException` — este código no
 * necesita capturarla: simplemente deja de ejecutarse ahí.
 */
class BirdFlightSimulator {
    suspend fun fly(durationSeconds: Int = 10, onTick: (elapsedSeconds: Int) -> Unit) {
        for (second in 1..durationSeconds) {
            delay(1000) // <- SUSPENSIÓN: el hilo queda libre durante este segundo
            onTick(second) // <- REANUDACIÓN: esto corre cuando el segundo ya pasó
        }
    }
}
