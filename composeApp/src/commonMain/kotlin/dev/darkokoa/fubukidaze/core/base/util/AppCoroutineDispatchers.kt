package dev.darkokoa.fubukidaze.core.base.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher

data class AppCoroutineDispatchers(
  val io: CoroutineDispatcher,
  val databaseWrite: CoroutineDispatcher,
  val databaseRead: CoroutineDispatcher,
  val computation: CoroutineDispatcher,
  val main: MainCoroutineDispatcher,
)
