package cl.almo.studyapp.demo.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Un Flow "frío" de movimientos de una cuenta: representa una RECETA de
 * emisión, no datos que ya existan en algún lado. No emite nada hasta que
 * alguien llama `collect()` sobre él — y si dos corrutinas distintas lo
 * coleccionan, el bloque de `flow { ... }` se ejecuta de cero para cada
 * una. Por eso es "frío": a diferencia de un StateFlow (que siempre tiene
 * un valor actual y no se re-ejecuta por cada colector nuevo), este Flow
 * no guarda nada hasta que alguien pregunta.
 */
fun accountMovementsFlow(onEmit: (String) -> Unit): Flow<String> = flow {
    val movimientos = listOf(
        "Compra café: -\$3.200",
        "Transferencia a un amigo: -\$15.000",
        "Devolución de compra: +\$8.900",
        "Pago de suscripción: -\$6.500"
    )
    onEmit("🏭 Productor: empieza a emitir")
    for (movimiento in movimientos) {
        delay(900)
        onEmit("🏭 Productor: emite \"$movimiento\"")
        emit(movimiento)
    }
    onEmit("🏭 Productor: terminó de emitir")
}
