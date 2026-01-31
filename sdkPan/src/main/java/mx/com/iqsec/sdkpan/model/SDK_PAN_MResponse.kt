package mx.com.iqsec.sdkpan.model

import androidx.annotation.Keep
import java.io.Serializable

@Keep
class SDK_PAN_MResponse(
    var estado: Int = 0,
    var descripcion: String = "",
    var no_transaccion: String = ""
) : Serializable