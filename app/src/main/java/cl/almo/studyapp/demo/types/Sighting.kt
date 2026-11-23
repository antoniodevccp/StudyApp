package cl.almo.studyapp.demo.types

/**
 * Un avistamiento de un animal. Es una `data class`: su identidad es el
 * contenido, no el objeto — igual que [Movement]. Dos Sighting con los
 * mismos campos son, a todo efecto, el mismo avistamiento.
 */
data class Sighting(
    val id: Int,
    val animal: String,
    val location: String
)
