package cl.almo.studyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cl.almo.studyapp.catalog.StudyAppRoot
import cl.almo.studyapp.ui.theme.StudyappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Sin Scaffold acá: cada pantalla (catálogo, y cada demo vía
            // DemoScaffold) tiene su propio Scaffold con su propia barra
            // superior. Un Scaffold externo más uno interno duplicaría el
            // padding por los insets del sistema (la barra de estado).
            StudyappTheme {
                StudyAppRoot()
            }
        }
    }
}