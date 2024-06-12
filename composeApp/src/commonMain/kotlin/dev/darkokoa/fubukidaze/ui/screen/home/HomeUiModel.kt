package dev.darkokoa.fubukidaze.ui.screen.home

import dev.darkokoa.fubukidaze.core.UiModel
import dev.darkokoa.fubukidaze.data.converter.toPojo
import dev.darkokoa.fubukidaze.data.db.dao.FubukidazeNodeDao
import dev.darkokoa.fubukidaze.data.model.pojo.FubukidazeNode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HomeUiModel(
  private val nodeDao: FubukidazeNodeDao,
) : UiModel<HomeUiState, HomeSideEffect>(HomeUiState()) {

  init {
    uiModelScope.launch(Dispatchers.Default) {
      nodeDao.allNodesFlow().onEach { resultsChange ->
        val fubukidazeNodeList = resultsChange.list.map { it.toPojo() }
        intent {
          reduce { it.copy(fubukidazeNodeList = fubukidazeNodeList) }
        }
      }.launchIn(this)
    }
  }

  fun onSelectChange(node: FubukidazeNode) = intent {
    val hasLaunch = uiState().hasLaunchFubuki

    if (hasLaunch) {
      postSideEffect(HomeSideEffect.SwitchFubukiConnection(node.config))
    }

    reduce { it.copy(selectedNode = node) }
  }

  fun onLaunchClick() = intent {
    val selectedNode = uiState().selectedNode
    if (selectedNode == null) {
      postSideEffect(HomeSideEffect.SnackbarMessage("No node selected"))
      return@intent
    }

    val hasLaunch = uiState().hasLaunchFubuki

    if (hasLaunch) {
      postSideEffect(HomeSideEffect.TerminateFubuki)
    } else {
      postSideEffect(HomeSideEffect.LaunchFubuki(selectedNode.config))
    }

    reduce { it.copy(hasLaunchFubuki = !hasLaunch) }
  }

}