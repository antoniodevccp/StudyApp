package cl.almo.studyapp.demo.types

/**
 * Un movimiento bancario. Es una `data class`: su identidad es el
 * contenido, no el objeto. Dos Movement con los mismos campos son, a todo
 * efecto, el mismo movimiento — por eso Kotlin genera `equals()`/
 * `hashCode()` comparando cada propiedad, además de `copy()` y un
 * `toString()` legible.
 */
data class Movement(
    val id: Int,
    val description: String,
    val amount: Int
)
