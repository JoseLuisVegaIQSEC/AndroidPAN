package mx.com.iqsec.sdkpan.domain.model

import androidx.annotation.Keep
import java.io.Serializable

@Keep
class StatesModel(
    val id_estado: Int,
    val nombre: String?,
    val abreviatura: String?
) : Serializable