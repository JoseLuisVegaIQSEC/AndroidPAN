package mx.com.iqsec.sdkpan.domain.model

import androidx.annotation.Keep
import java.io.Serializable

@Keep
class SaveSignModel(
    val estado: Int?,
    val descripcion: String?,
    val folio: Int?,
    val referencia: String?
) : Serializable