package dev.darkokoa.fubukidaze.core.flow

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

typealias EventFlow<T> = MutableSharedFlow<T>

@Suppress("FunctionName")
fun <T> EventFlow(replay: Int = 0): EventFlow<T> = MutableSharedFlow(
  replay = replay,
  extraBufferCapacity = 1,
  onBufferOverflow = BufferOverflow.DROP_OLDEST
)

fun <T> EventFlow<T>.send(event: T) {
  tryEmit(event)
}

fun <T> EventFlow<T>.sendBlock(eventBlock: () -> T) {
  val event = eventBlock.invoke()

  send(event)
}