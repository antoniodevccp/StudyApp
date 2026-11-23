package cl.almo.studyapp.catalog

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cl.almo.studyapp.demo.coroutines.CoroutinesDemoScreen
import cl.almo.studyapp.demo.flow.FlowDemoScreen
import cl.almo.studyapp.demo.lifecycle.LifecycleDemoScreen
import cl.almo.studyapp.demo.structured.StructuredConcurrencyScreen
import cl.almo.studyapp.demo.types.TypesDemoScreen

/**
 * Raíz de navegación de la app. No usa Navigation-Compose: con un solo
 * "destino actual" guardado en estado y un `when` alcanza — no hay deep
 * links, ni pila de más de un nivel, ni argumentos complejos entre
 * pantallas. [BackHandler] (de `androidx.activity:activity-compose`, ya
 * dependencia del proyecto) intercepta el botón/gesto Atrás del sistema
 * para volver al catálogo en vez de cerrar la Activity.
 */
@Composable
fun StudyAppRoot(modifier: Modifier = Modifier) {
    var destination by remember { mutableStateOf<Destination>(Destination.Catalog) }

    BackHandler(enabled = destination != Destination.Catalog) {
        destination = Destination.Catalog
    }

    Box(modifier = modifier.fillMaxSize()) {
        when (val current = destination) {
            Destination.Catalog -> CatalogScreen(
                onSelect = { id -> destination = Destination.Demo(id) }
            )
            is Destination.Demo -> DemoHost(
                id = current.id,
                onBack = { destination = Destination.Catalog }
            )
        }
    }
}

/** Dentro de una demo: decide cuál pantalla mostrar según el [ConceptId]. */
@Composable
private fun DemoHost(id: ConceptId, onBack: () -> Unit) {
    when (id) {
        ConceptId.COROUTINES -> CoroutinesDemoScreen(onBack = onBack)
        ConceptId.FLOW -> FlowDemoScreen(onBack = onBack)
        ConceptId.STRUCTURED_CONCURRENCY -> StructuredConcurrencyScreen(onBack = onBack)
        ConceptId.COMPOSE_LIFECYCLE -> LifecycleDemoScreen(onBack = onBack)
        ConceptId.KOTLIN_TYPES -> TypesDemoScreen(onBack = onBack)
    }
}
