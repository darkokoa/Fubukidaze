package dev.darkokoa.fubukidaze.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun ColumnScope.BlankSpacer(height: Dp) {
  Spacer(modifier = Modifier.height(height))
}

@Composable
fun RowScope.BlankSpacer(width: Dp) {
  Spacer(modifier = Modifier.width(width))
}