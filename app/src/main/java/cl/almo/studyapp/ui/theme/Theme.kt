package cl.almo.studyapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

/**
 * Tema de la app: paleta Gengar centralizada en [LightColors]/[DarkColors]
 * (Color.kt), formas redondeadas en [Shapes] (Shape.kt), y la escala
 * tipográfica de Material 3 en [Typography] (Type.kt — solo bodyLarge
 * está ajustado a mano, el resto usa los valores por defecto de M3).
 *
 * No hay color dinámico (Material You) a propósito: en Android 12+ eso
 * generaría el esquema de color a partir del fondo de pantalla del
 * usuario, pisando la paleta Gengar que es justamente el pedido de diseño
 * de esta app.
 */
@Composable
fun StudyappTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
