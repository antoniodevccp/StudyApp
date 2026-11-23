package cl.almo.studyapp.demo.types

/**
 * Contrato para obtener avistamientos — el equivalente de [MovementSource]
 * para el dominio de animales. [Bird] no necesita saber de dónde vienen
 * los avistamientos, solo pedirlos; estas dos implementaciones son
 * intercambiables entre sí.
 */
interface SightingSource {
    val label: String
    fun loadSightings(): List<Sighting>
}

/** Avistamientos fijos de un bosque ficticio. */
class ForestSightingSource : SightingSource {
    override val label: String = "Bosque (demo)"

    override fun loadSightings(): List<Sighting> = listOf(
        Sighting(id = 1, animal = "Conejo", location = "Claro del bosque"),
        Sighting(id = 2, animal = "Zorro", location = "Orilla del río"),
        Sighting(id = 3, animal = "Búho", location = "Copa del roble")
    )
}

/** Avistamientos fijos de un humedal ficticio. */
class WetlandSightingSource : SightingSource {
    override val label: String = "Humedal (demo)"

    override fun loadSightings(): List<Sighting> = listOf(
        Sighting(id = 101, animal = "Garza", location = "Laguna"),
        Sighting(id = 102, animal = "Nutria", location = "Canal")
    )
}
