package dev.darkokoa.fubukidaze.ui.screen

import dev.darkokoa.fubukidaze.ui.screen.nodeeditor.NodeEditorUiModel
import dev.darkokoa.fubukidaze.ui.screen.home.HomeUiModel
import dev.darkokoa.fubukidaze.ui.screen.fubukillog.FubukilLogUiModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val uiModelModule = module {
  factoryOf(::NodeEditorUiModel)
  factoryOf(::HomeUiModel)
  factoryOf(::FubukilLogUiModel)
}