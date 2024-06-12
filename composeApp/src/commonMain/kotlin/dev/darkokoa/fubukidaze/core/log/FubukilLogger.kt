package dev.darkokoa.fubukidaze.core.log

import kotlinx.coroutines.flow.Flow

expect class FubukilLogger() {

  fun init()

  fun logStream(): Flow<String>
}