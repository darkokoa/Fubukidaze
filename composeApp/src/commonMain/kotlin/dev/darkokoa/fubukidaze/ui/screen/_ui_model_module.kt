package dev.darkokoa.fubukidaze.ui.screen

import dev.darkokoa.fubukidaze.ui.screen.configeditor.ConfigEditorUiModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val uiModelModule = module {
  factoryOf(::ConfigEditorUiModel)
}