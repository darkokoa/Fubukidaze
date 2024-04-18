package dev.darkokoa.fubukidaze.android.util.fubuki

import com.sun.jna.*
import com.sun.jna.ptr.PointerByReference

@Suppress("PropertyName")
interface FubukiJNA : Library {

  companion object {
    val INSTANCE: FubukiJNA by lazy { Native.load("fubukil", FubukiJNA::class.java) }

    const val FUBUKI_START_OPTIONS_VERSION = 2
  }

  @Structure.FieldOrder(
    "ctx",
    "node_config_json",
    "device_index",
    "fubuki_to_if_fn",
    "add_addr_fn",
    "delete_addr_fn",
    "tun_fd"
  )
  class FubukiStartOptions : Structure() {
    @JvmField
    var ctx: Pointer? = null

    @JvmField
    var node_config_json: String? = null

    @JvmField
    var device_index: Int = 0

    @JvmField
    var fubuki_to_if_fn: FubukiToIfFn? = null

    @JvmField
    var add_addr_fn: AddAddrFn? = null

    @JvmField
    var delete_addr_fn: DeleteAddrFn? = null

    @JvmField
    var tun_fd: Int = 0

    fun interface FubukiToIfFn : Callback {
      fun invoke(packet: Pointer, len: Int, ctx: Pointer)
    }

    fun interface AddAddrFn : Callback {
      fun invoke(addr: Int, netmask: Int, ctx: Pointer)
    }

    fun interface DeleteAddrFn : Callback {
      fun invoke(addr: Int, netmask: Int, ctx: Pointer)
    }
  }

  class FubukiHandle : PointerType()

  fun if_to_fubuki(handle: FubukiHandle, packet: ByteArray, len: Int)

  fun fubuki_start(
    options: FubukiStartOptions,
    version: Int = FUBUKI_START_OPTIONS_VERSION,
    error: PointerByReference = PointerByReference(),
  ): FubukiHandle

  fun fubuki_stop(handle: FubukiHandle)
}