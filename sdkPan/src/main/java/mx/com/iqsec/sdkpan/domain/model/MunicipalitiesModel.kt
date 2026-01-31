package mx.com.iqsec.sdkpan.domain.model

import androidx.annotation.Keep
import java.io.Serializable

@Keep
class MunicipalitiesModel(
    val id_municipio: Int,
    val nombre: String?
) : Serializable