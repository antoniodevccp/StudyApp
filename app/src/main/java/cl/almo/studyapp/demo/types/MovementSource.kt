package cl.almo.studyapp.demo.types

/**
 * Contrato para obtener el historial de movimientos de una cuenta.
 * [Account] no necesita saber de dónde vienen —memoria, red, una base de
 * datos— solo pedirlos. Eso permite cambiar la fuente sin tocar Account ni
 * la pantalla: las dos implementaciones de abajo son intercambiables entre
 * sí.
 */
interface MovementSource {
    val label: String
    fun loadMovements(): List<Movement>
}

/** Historial fijo de una cuenta corriente ficticia. */
class CheckingAccountSource : MovementSource {
    override val label: String = "Cuenta corriente (demo)"

    override fun loadMovements(): List<Movement> = listOf(
        Movement(id = 1, description = "Compra supermercado", amount = -15000),
        Movement(id = 2, description = "Transferencia recibida", amount = 50000),
        Movement(id = 3, description = "Pago cuenta de luz", amount = -22000)
    )
}

/** Historial fijo de una cuenta de ahorro ficticia. */
class SavingsAccountSource : MovementSource {
    override val label: String = "Cuenta de ahorro (demo)"

    override fun loadMovements(): List<Movement> = listOf(
        Movement(id = 101, description = "Depósito mensual programado", amount = 100000),
        Movement(id = 102, description = "Intereses del mes", amount = 1200)
    )
}
