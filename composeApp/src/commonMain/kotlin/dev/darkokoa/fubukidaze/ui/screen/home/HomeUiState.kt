package dev.darkokoa.fubukidaze.ui.screen.home

import dev.darkokoa.fubukidaze.data.model.pojo.FubukiNodeConfig
import dev.darkokoa.fubukidaze.data.model.pojo.FubukidazeNode
import dev.darkokoa.fubukidaze.data.model.pojo.Group

data class HomeUiState(
  val hasLaunchFubuki: Boolean = false,
  val fubukidazeNodeList: List<FubukidazeNode> = emptyList(),
  val selectedNode: FubukidazeNode? = null,
  val loading: Boolean = false,
  val processing: Boolean = false,
)