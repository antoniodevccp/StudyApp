package cl.almo.studyapp.demo.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Flow frío que simula una cámara trampa del bosque: detecta un conejo,
 * un zorro y un búho, uno por uno. No emite nada hasta que alguien
 * colecciona este Flow con `collect`, y cada colección nueva vuelve a
 * ejecutar este bloque desde el principio — por eso la secuencia siempre
 * arranca otra vez por el conejo.
 */
fun forestCameraFlow(onEmit: (String) -> Unit): Flow<String> = flow {
    val animales = listOf("🐇 Conejo", "🦊 Zorro", "🦉 Búho")
    onEmit("📷 Cámara: empieza a observar")
    for (animal in animales) {
        delay(900)
        onEmit("📷 Cámara: detecta a $animal")
        emit(animal)
    }
    onEmit("📷 Cámara: termina de observar")
}
