package dev.darkokoa.fubukidaze.ui.component

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Edit
import compose.icons.feathericons.Edit3
import compose.icons.feathericons.Server
import dev.darkokoa.fubukidaze.data.model.pojo.FubukidazeNode
import dev.darkokoa.fubukidaze.ui.BlankSpacer

@Composable
fun FubukidazeNodeItem(
  node: FubukidazeNode,
  isSelect: Boolean,
  onItemClick: (node: FubukidazeNode) -> Unit,
  onEditClick: (node: FubukidazeNode) -> Unit,
  modifier: Modifier = Modifier
) {
  Crossfade(targetState = isSelect) {
    val (containerColor, contentColor) = if (it) {
      Pair(
        MaterialTheme.colorScheme.tertiaryContainer,
        MaterialTheme.colorScheme.onTertiaryContainer,
      )
    } else {
      Pair(
        MaterialTheme.colorScheme.surface,
        MaterialTheme.colorScheme.onSurface,
      )
    }
    Surface(
      onClick = { onItemClick(node) },
      modifier = modifier,
      color = containerColor,
      contentColor = contentColor
    ) {
      Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        BlankSpacer(width = 8.dp)

        Icon(imageVector = FeatherIcons.Server, contentDescription = null)

        BlankSpacer(width = 24.dp)

        val titleText = node.name ?: "Unnamed"
        val addrsText = buildString {
          node.config.groups.forEachIndexed { index, group ->
            append("${group.node_name ?: ""}[${group.server_addr}]")
            if (index != node.config.groups.lastIndex) {
              append(", ")
            }
          }
        }

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = titleText,
            style = MaterialTheme.typography.titleLarge,
          )
          BlankSpacer(height = 4.dp)
          Text(
            text = addrsText,
            style = MaterialTheme.typography.bodyMedium,
          )
        }
        BlankSpacer(width = 8.dp)
        IconButton(onClick = { onEditClick(node) }) {
          Icon(imageVector = FeatherIcons.Edit3, contentDescription = "edit")
        }
      }
    }
  }
}
