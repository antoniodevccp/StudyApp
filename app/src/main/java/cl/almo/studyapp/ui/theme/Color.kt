package cl.almo.studyapp.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Paleta inspirada en Gengar: violeta profundo + lavanda como colores de
// marca (acciones principales en claro/oscuro), un rosa rojizo como acento
// puntual, y fondos muy claro/muy oscuro a juego. Estos cuatro son los
// valores de partida; el resto de los roles de Material 3 se derivan a
// mano abajo para mantener buen contraste en los dos temas.

val GengarVioletLight = Color(0xFF6035A8) // tema claro: primary
val GengarLavenderDark = Color(0xFFCDB4FF) // tema oscuro: primary
val GengarBackgroundLight = Color(0xFFF8F4FF)
val GengarBackgroundDark = Color(0xFF171222)

// Acento puntual. Ojo: su contraste con texto blanco (≈2.75:1) no alcanza
// el mínimo de accesibilidad para texto (4.5:1) sobre el fondo claro —
// por eso nunca se usa como color de texto suelto, siempre como relleno
// de un contenedor (tertiaryContainer) con su onTertiary oscuro encima.
val GengarAccent = Color(0xFFF06A8A)
private val OnGengarAccent = Color(0xFF3B0318)

val LightColors = lightColorScheme(
    primary = GengarVioletLight,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEADDFF),
    onPrimaryContainer = Color(0xFF28004D),

    secondary = Color(0xFF6F5B7E),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFEDE0F5),
    onSecondaryContainer = Color(0xFF271930),

    tertiary = GengarAccent,
    onTertiary = OnGengarAccent,
    tertiaryContainer = Color(0xFFFFD9E2),
    onTertiaryContainer = Color(0xFF5C1228),

    background = GengarBackgroundLight,
    onBackground = Color(0xFF1C1328),
    surface = GengarBackgroundLight,
    onSurface = Color(0xFF1C1328),
    surfaceVariant = Color(0xFFE7E0F0),
    onSurfaceVariant = Color(0xFF49454E),
    outline = Color(0xFF7B7584)
)

val DarkColors = darkColorScheme(
    primary = GengarLavenderDark,
    onPrimary = Color(0xFF2A0F52),
    primaryContainer = Color(0xFF4A2585),
    onPrimaryContainer = Color(0xFFEADDFF),

    secondary = Color(0xFFCBB8D9),
    onSecondary = Color(0xFF362C41),
    secondaryContainer = Color(0xFF4E4259),
    onSecondaryContainer = Color(0xFFE6D7F0),

    tertiary = GengarAccent,
    onTertiary = OnGengarAccent,
    tertiaryContainer = Color(0xFF5C1228),
    onTertiaryContainer = Color(0xFFFFD9E2),

    background = GengarBackgroundDark,
    onBackground = Color(0xFFEAE3F7),
    surface = Color(0xFF1E1830),
    onSurface = Color(0xFFEAE3F7),
    surfaceVariant = Color(0xFF2B2440),
    onSurfaceVariant = Color(0xFFCBC2DC),
    outline = Color(0xFF948F9E)
)
