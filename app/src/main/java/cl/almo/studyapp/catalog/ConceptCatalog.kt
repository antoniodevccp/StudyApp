package cl.almo.studyapp.catalog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Identifica cada demo del catálogo. Un `enum` alcanza acá: son casos fijos
 * y conocidos de antemano, no una jerarquía que crezca con subtipos propios
 * (para eso está `sealed`, ver [cl.almo.studyapp.demo.types.WithdrawalResult]).
 */
enum class ConceptId {
    COROUTINES,
    FLOW,
    STRUCTURED_CONCURRENCY,
    COMPOSE_LIFECYCLE,
    KOTLIN_TYPES
}

/** Los tres grupos en que se organiza el catálogo. */
enum class ConceptCategory(val label: String) {
    KOTLIN("Kotlin"),
    ASYNC("Programación asíncrona"),
    COMPOSE("Jetpack Compose")
}

/** Lo que se muestra de cada concepto en la lista del catálogo. */
data class ConceptItem(
    val id: ConceptId,
    val title: String,
    val summary: String,
    val category: ConceptCategory,
    val icon: ImageVector
)

val concepts: List<ConceptItem> = listOf(
    ConceptItem(
        id = ConceptId.COROUTINES,
        title = "Corrutinas",
        summary = "Probá cargar datos sin bloquear la pantalla: suspend, delay y cancelación.",
        category = ConceptCategory.ASYNC,
        icon = Icons.Default.Sync
    ),
    ConceptItem(
        id = ConceptId.FLOW,
        title = "Flow",
        summary = "Probá coleccionar un flujo frío y ver qué pasa si lo coleccionás dos veces.",
        category = ConceptCategory.ASYNC,
        icon = Icons.Default.Timeline
    ),
    ConceptItem(
        id = ConceptId.STRUCTURED_CONCURRENCY,
        title = "Job y CoroutineScope",
        summary = "Probá cancelar una tarea puntual y, después, todo el scope que las agrupa.",
        category = ConceptCategory.ASYNC,
        icon = Icons.Default.AccountTree
    ),
    ConceptItem(
        id = ConceptId.COMPOSE_LIFECYCLE,
        title = "Ciclo de vida en Compose",
        summary = "Probá ocultar y mostrar un componente y ver qué pasa con su estado.",
        category = ConceptCategory.COMPOSE,
        icon = Icons.Default.Visibility
    ),
    ConceptItem(
        id = ConceptId.KOTLIN_TYPES,
        title = "Tipos de Kotlin",
        summary = "Probá class, data class, interface y sealed interface en un caso real.",
        category = ConceptCategory.KOTLIN,
        icon = Icons.Default.Category
    )
)
