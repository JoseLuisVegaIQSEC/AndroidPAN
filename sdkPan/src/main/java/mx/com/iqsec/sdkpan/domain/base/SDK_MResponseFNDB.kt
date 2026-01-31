package mx.com.iqsec.sdkpan.domain.base

import java.io.Serializable

data class SDK_MResponseFNDB<T>(
    val codigo: Int,
    val message: Pair<String, String> = Pair("", ""),
    val codService: Int = 0,
    val messageService: String = "",
    val data: T
) : Serializable