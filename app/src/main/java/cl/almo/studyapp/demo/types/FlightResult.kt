package cl.almo.studyapp.demo.types

/**
 * Resultado posible de intentar volar — el equivalente de
 * [WithdrawalResult] para el dominio de animales. El `when` que lo maneja
 * en [TypesDemoScreen] no tiene rama `else`: como esta interfaz es
 * `sealed`, el compilador conoce las dos variantes y exige cubrir ambas.
 */
sealed interface FlightResult {
    data class Exitoso(val energiaRestante: Int) : FlightResult
    data class EnergiaInsuficiente(val energiaActual: Int, val energiaRequerida: Int) : FlightResult
}
