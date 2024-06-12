package dev.darkokoa.fubukidaze.core.log

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

actual class FubukilLogger {

  actual fun init() {
    Runtime.getRuntime().exec("logcat -c")
  }

  actual fun logStream(): Flow<String> = flow {
    Runtime.getRuntime().exec("logcat")?.inputStream?.bufferedReader()?.useLines { lines ->
      lines.forEach { line ->
        if (line.contains("fubukil")) {
          emit(line + "\n")
        }
      }
    }
  }.flowOn(Dispatchers.IO)

}