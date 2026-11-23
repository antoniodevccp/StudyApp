package cl.almo.studyapp.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Formas más redondeadas que el valor por defecto de Material 3, para una
 * sensación más "burbuja" acorde a la paleta Gengar. `RoundedCornerShape`
 * ya es parte de Compose Foundation — no hace falta ninguna dependencia
 * nueva para esto.
 */
val Shapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp)
)
