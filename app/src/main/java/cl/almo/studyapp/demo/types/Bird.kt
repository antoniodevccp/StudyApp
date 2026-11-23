package cl.almo.studyapp.demo.types

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Encapsula el comportamiento de un pájaro: cuánta energía tiene y las
 * reglas de cuándo puede volar — el equivalente de [Account] para el
 * dominio de animales. Es una `class`: `energy` solo cambia a través de
 * [fly], nada de afuera puede pisarlo directamente.
 */
class Bird(
    initialEnergy: Int,
    initialSource: SightingSource
) {
    private var source: SightingSource = initialSource

    var energy: Int by mutableStateOf(initialEnergy)
        private set

    var sightings: List<Sighting> by mutableStateOf(initialSource.loadSightings())
        private set

    val sourceLabel: String get() = source.label

    fun changeSource(newSource: SightingSource) {
        source = newSource
        sightings = newSource.loadSightings()
    }

    fun fly(energyCost: Int): FlightResult {
        return if (energyCost > energy) {
            FlightResult.EnergiaInsuficiente(energiaActual = energy, energiaRequerida = energyCost)
        } else {
            energy -= energyCost
            FlightResult.Exitoso(energiaRestante = energy)
        }
    }
}
