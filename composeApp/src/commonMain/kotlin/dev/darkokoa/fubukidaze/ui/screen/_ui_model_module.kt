package dev.darkokoa.fubukidaze.ui.screen

import dev.darkokoa.fubukidaze.ui.screen.fbkconfigeditor.viajson.FubukiJsonConfigEditorUiModel
import dev.darkokoa.fubukidaze.ui.screen.fbkconfigeditor.viaparams.FubukiParamsConfigEditorUiModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val uiModelModule = module {
  factoryOf(::FubukiParamsConfigEditorUiModel)
  factoryOf(::FubukiJsonConfigEditorUiModel)
}