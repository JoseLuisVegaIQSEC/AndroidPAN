package mx.com.iqsec.sdkpan.data.dtos

import com.google.gson.annotations.SerializedName

data class AntispoffingDTO(
    @SerializedName("cod_error")
    val codError: Int,
    @SerializedName("descripcion")
    val descripcion: String?,
    @SerializedName("data")
    val data: AntispoffingDataDTO
)

// Tu DTO ya existente para "data"
data class AntispoffingDataDTO(
    @SerializedName("analisis")
    val analisis: String?,
    @SerializedName("promedio")
    val promedio: Double?,
    @SerializedName("indices")
    val indices: Array<Int>?
)