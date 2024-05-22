import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.awt.Dimension
import dev.darkokoa.fubukidaze.App
import dev.darkokoa.fubukidaze.core.koin.KoinInitializer

fun main() = application {
  KoinInitializer.initialize()
  Window(
    title = "Fubukidaze",
    state = rememberWindowState(width = 360.dp, height = 720.dp),
    onCloseRequest = ::exitApplication,
  ) {
    window.minimumSize = Dimension(360, 720)
    App()
  }
}