package dev.darkokoa.fubukidaze.ui.screen.home

import dev.darkokoa.fubukidaze.data.model.pojo.FubukidazeNode

data class HomeUiState(
  val hasLaunchFubuki: Boolean = false,
  val fubukidazeNodeList: List<FubukidazeNode> = emptyList(),
  val selectedNodeId: String? = null,
  val loading: Boolean = false,
  val processing: Boolean = false,
)