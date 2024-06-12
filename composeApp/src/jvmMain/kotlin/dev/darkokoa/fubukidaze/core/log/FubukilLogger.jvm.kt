package dev.darkokoa.fubukidaze.core.log

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

actual class FubukilLogger {

  actual fun init() {}

  actual fun logStream(): Flow<String> = flow { }

}