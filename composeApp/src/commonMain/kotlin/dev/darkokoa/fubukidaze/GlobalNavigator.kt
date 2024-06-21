package dev.darkokoa.fubukidaze

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

typealias GlobalNavigatorAction = Navigator.() -> Unit

object GlobalNavigator : KoinComponent {

  private val navigateActionChannel = Channel<GlobalNavigatorAction>(Channel.BUFFERED)

  @Composable
  fun register(
    navigator: Navigator,
    coroutineScope: CoroutineScope
  ) {
    LaunchedEffect(navigator) {
      navigateActionChannel.receiveAsFlow().onEach {
        coroutineScope.launch(Dispatchers.Main.immediate) {
          it(navigator)
        }
      }.launchIn(coroutineScope)
    }
  }

  fun tryTransaction(action: GlobalNavigatorAction) {
    navigateActionChannel.trySend(action)
  }

  suspend fun transaction(action: GlobalNavigatorAction) {
    navigateActionChannel.send(action)
  }

}