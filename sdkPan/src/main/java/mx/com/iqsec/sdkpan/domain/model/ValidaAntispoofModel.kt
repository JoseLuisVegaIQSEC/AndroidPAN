package mx.com.iqsec.sdkpan.domain.model

import androidx.annotation.Keep

@Keep
class AntispoffingModel(
    val codError: Int,
    val descripcion: String?,
    val data: AntispoffingDataModel
)

@Keep
class AntispoffingDataModel(
    val analisis: String?,
    val promedio: Double?,
    val indices: Array<Int>?
)
