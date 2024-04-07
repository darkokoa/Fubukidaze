import androidx.compose.ui.window.ComposeUIViewController
import dev.darkokoa.fubukidaze.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
