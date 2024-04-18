package dev.darkokoa.fubukidaze.core.base.util

fun prefixLengthToNetmask(prefixLength: Int): String {
  require(prefixLength in 0..32) { "Prefix length must be between 0 and 32" }

  val mask = -1 shl (32 - prefixLength)

  return buildString {
    append((mask shr 24) and 0xFF)
    append('.')
    append((mask shr 16) and 0xFF)
    append('.')
    append((mask shr 8) and 0xFF)
    append('.')
  }
}


fun netmaskToPrefixLength(netmask: String): Int {
  val parts = netmask.split(".").map { it.toInt() }
  if (parts.size != 4 || parts.any { it !in 0..255 }) {
    throw IllegalArgumentException("Invalid netmask format")
  }

  fun Int.toBinaryString(): String {
    return this.toString(2).padStart(8, '0')
  }

  val binaryString = parts.joinToString("") { it.toBinaryString() }
  return binaryString.count { it == '1' }
}