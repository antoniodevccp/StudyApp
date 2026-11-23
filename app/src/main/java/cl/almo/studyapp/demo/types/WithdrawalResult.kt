package cl.almo.studyapp.demo.types

/**
 * Resultado posible de un retiro, como jerarquía cerrada. El `when` de
 * [TypesDemoScreen] que lo maneja no tiene rama `else`: como esta interfaz
 * es `sealed`, el compilador conoce las dos variantes y exige cubrir
 * ambas.
 *
 * ¿Cuándo `sealed class` en vez de `sealed interface`? Acá da lo mismo
 * porque cada variante ya es su propia `data class` con todas las
 * propiedades que necesita — no hay estado ni comportamiento común que una
 * superclase pudiera darles gratis. Elegirías `sealed class` cuando las
 * variantes SÍ comparten algo real: una propiedad común con el mismo valor
 * por defecto, un constructor con lógica, o un método con cuerpo que todas
 * usarían igual (algo que una interfaz no puede guardar como estado).
 */
sealed interface WithdrawalResult {
    data class Aprobado(val nuevoSaldo: Int) : WithdrawalResult
    data class SaldoInsuficiente(val saldoActual: Int, val montoSolicitado: Int) : WithdrawalResult
}
