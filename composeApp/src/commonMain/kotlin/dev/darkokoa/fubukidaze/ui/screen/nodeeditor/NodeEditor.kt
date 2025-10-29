package dev.darkokoa.fubukidaze.ui.screen.nodeeditor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import compose.icons.feathericons.Save
import compose.icons.feathericons.Trash2
import dev.darkokoa.fubukidaze.core.serializable.CommonSerializable
import dev.darkokoa.fubukidaze.ui.BlankSpacer
import dev.darkokoa.fubukidaze.ui.screen.Screen
import org.koin.core.parameter.parametersOf

class NodeEditor(
  private val mode: Mode = Mode.Create
) : Screen {

  sealed interface Mode : CommonSerializable {
    data object Create : Mode
    data class Edit(val id: String) : Mode
  }

  @Composable
  override fun Content(
    navigator: Navigator,
    bottomSheetNavigator: BottomSheetNavigator
  ) {

    val uiModel = koinScreenModel<NodeEditorUiModel>(parameters = { parametersOf(mode) })
    val uiState by uiModel.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val imeController = LocalSoftwareKeyboardController.current
    val configTextFieldFocusRequester = remember { FocusRequester() }


    uiModel.collectSideEffect {
      when (it) {
        is NodeEditorSideEffect.SnackbarMessage -> snackbarHostState.showSnackbar(it.message)
        NodeEditorSideEffect.NavUp -> navigator.pop()
      }
    }

    DisposableEffect(Unit) {
      onDispose {
        runCatching {
          configTextFieldFocusRequester.freeFocus()
        }
        imeController?.hide()
      }
    }

    LaunchedEffect(Unit) {
      if (mode == Mode.Create) {
        configTextFieldFocusRequester.requestFocus()
      }
    }

    ConfigEditorContent(
      mode = mode,
      uiState = uiState,
      snackbarHostState = snackbarHostState,
      configTextFieldFocusRequester = configTextFieldFocusRequester,
      onBack = {
        imeController?.hide()
        navigator.pop()
      },
      onDelete = uiModel::onDelete,
      onSave = uiModel::onSave,
      onNodeNameInputChange = uiModel::onNodeNameInputChange,
      onConfigInputChange = uiModel::onConfigInputChange
    )
  }
}

@Composable
private fun ConfigEditorContent(
  mode: NodeEditor.Mode,
  uiState: NodeEditorUiState,
  snackbarHostState: SnackbarHostState,
  configTextFieldFocusRequester: FocusRequester,
  onBack: () -> Unit,
  onDelete: (String) -> Unit,
  onSave: () -> Unit,
  onNodeNameInputChange: (TextFieldValue) -> Unit,
  onConfigInputChange: (TextFieldValue) -> Unit,
  modifier: Modifier = Modifier
) {
  val topBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

  Scaffold(
    topBar = {
      ConfigEditorTopBar(
        mode = mode,
        onBack = onBack,
        onDelete = onDelete,
        onSave = onSave,
        canSave = uiState.canSave,
        scrollBehavior = topBarScrollBehavior
      )
    },
    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    modifier = modifier.imePadding().nestedScroll(topBarScrollBehavior.nestedScrollConnection)
  ) { paddingValues ->
    Column(
      modifier = Modifier.fillMaxSize()
        .padding(paddingValues)
        .padding(horizontal = 16.dp)
        .verticalScroll(rememberScrollState())
    ) {
      BlankSpacer(16.dp)

      OutlinedTextField(
        value = uiState.nodeName,
        onValueChange = onNodeNameInputChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Name (Optional)") },
        minLines = 1,
      )

      BlankSpacer(16.dp)

      OutlinedTextField(
        value = uiState.configJsonString,
        onValueChange = onConfigInputChange,
        modifier = Modifier.fillMaxWidth().focusRequester(configTextFieldFocusRequester),
        isError = uiState.configParsingFailed,
        label = { Text("Fubuki Node Config") },
        minLines = 10,
      )

      BlankSpacer(16.dp)
    }
  }
}

@Composable
private fun ConfigEditorTopBar(
  mode: NodeEditor.Mode,
  onBack: () -> Unit,
  onDelete: (String) -> Unit,
  onSave: () -> Unit,
  canSave: Boolean,
  scrollBehavior: TopAppBarScrollBehavior,
  modifier: Modifier = Modifier,
) {
  LargeTopAppBar(
    title = { Text("Node Config") },
    modifier = modifier,
    navigationIcon = {
      IconButton(onClick = onBack) {
        Icon(imageVector = FeatherIcons.ArrowLeft, contentDescription = "back")
      }
    },
    actions = {
      if (mode is NodeEditor.Mode.Edit) {
        IconButton(onClick = { onDelete(mode.id) }) {
          Icon(imageVector = FeatherIcons.Trash2, contentDescription = "delete")
        }
      }
      IconButton(onClick = onSave, enabled = canSave) {
        Icon(imageVector = FeatherIcons.Save, contentDescription = "save")
      }
    },
    scrollBehavior = scrollBehavior
  )
}
