package mx.com.iqsec.sdkpan.domain.base

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SDK_FNResponse<T>(
    @SerializedName("cod_error") val codigo: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: T
): Serializable