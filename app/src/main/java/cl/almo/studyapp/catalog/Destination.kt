package cl.almo.studyapp.catalog

/**
 * Los dos lugares posibles de la app: el catálogo, o una demo puntual.
 * `sealed interface` en vez de un enum porque `Demo` necesita cargar un
 * dato (`id`) y un enum no puede tener una variante con forma distinta.
 */
sealed interface Destination {
    data object Catalog : Destination
    data class Demo(val id: ConceptId) : Destination
}
