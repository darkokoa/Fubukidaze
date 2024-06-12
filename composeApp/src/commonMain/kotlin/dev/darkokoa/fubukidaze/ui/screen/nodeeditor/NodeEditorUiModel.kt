package dev.darkokoa.fubukidaze.ui.screen.nodeeditor

import androidx.compose.ui.text.input.TextFieldValue
import dev.darkokoa.fubukidaze.core.UiModel
import dev.darkokoa.fubukidaze.data.db.dao.FubukidazeNodeDao
import dev.darkokoa.fubukidaze.data.model.entity.FubukidazeNodeEntity
import dev.darkokoa.fubukidaze.data.model.pojo.FubukiNodeConfig
import kotlinx.serialization.json.Json

class NodeEditorUiModel(
  private val mode: NodeEditor.Mode,
  private val nodeDao: FubukidazeNodeDao,
) : UiModel<NodeEditorUiState, NodeEditorSideEffect>(NodeEditorUiState()) {

  init {
    if (mode is NodeEditor.Mode.Edit) {
      intent {
        nodeDao.findById(mode.id).find()?.let { nodeEntity ->
          reduce {
            it.copy(
              nodeName = TextFieldValue(nodeEntity.name ?: ""),
              configJsonString = TextFieldValue(nodeEntity.rawConfig),
              canSave = true
            )
          }
        }
      }
    }
  }

  fun onNodeNameInputChange(input: TextFieldValue) = intent {
    reduce {
      it.copy(nodeName = input)
    }
  }

  fun onConfigInputChange(configJsonString: TextFieldValue) = intent {
    reduce {
      it.copy(
        configJsonString = configJsonString,
        canSave = configJsonString.text.trim().isNotEmpty(),
        configParsingFailed = false
      )
    }
  }

  fun onSave() = intent {
    reduce { it.copy(processing = true) }

    val configJsonString = uiState().configJsonString.text.trim()

    runCatching {
      Json.decodeFromString<FubukiNodeConfig>(configJsonString)
    }.onSuccess {
      if (it.groups.isEmpty()) {
        reduce { state -> state.copy(configParsingFailed = true, processing = false) }
        postSideEffect(NodeEditorSideEffect.SnackbarMessage("groups cannot be empty ❌"))

        return@intent
      }

      when (mode) {
        NodeEditor.Mode.Create -> {
          handleAddNode(
            nodeName = uiState().nodeName.text.trim(),
            configJsonString = configJsonString
          )
        }

        is NodeEditor.Mode.Edit -> {
          handleUpdateNode(
            nodeId = mode.id,
            nodeName = uiState().nodeName.text.trim(),
            configJsonString = configJsonString
          )
        }
      }
      reduce { it.copy(processing = false) }
      postSideEffect(NodeEditorSideEffect.NavUp)

    }.onFailure { throwable ->
      reduce { it.copy(configParsingFailed = true, processing = false) }

      postSideEffect(NodeEditorSideEffect.SnackbarMessage("Config parsing failed ❌" + "\n\n${throwable.message}"))

      throwable.printStackTrace()
    }
  }

  fun onDelete(id: String) = intent {
    reduce { it.copy(processing = true) }

    nodeDao.deleteById(id)

    reduce { it.copy(processing = false) }
    postSideEffect(NodeEditorSideEffect.NavUp)
  }

  private suspend fun handleAddNode(
    nodeName: String,
    configJsonString: String,
  ) {
    nodeDao.upsert(
      FubukidazeNodeEntity().apply {
        name = nodeName
        rawConfig = configJsonString
      }
    )
  }

  private suspend fun handleUpdateNode(
    nodeId: String,
    nodeName: String,
    configJsonString: String,
  ) {
    val nodeEntity = FubukidazeNodeEntity().apply {
      id = nodeId
      name = nodeName
      rawConfig = configJsonString
    }

    nodeDao.upsert(nodeEntity)
  }
}