package mx.com.iqsec.sdkpan.data.dtos

import com.google.gson.annotations.SerializedName

class VerifyFramesDTO(
    @SerializedName("estado")
    val estado: Int?,

    @SerializedName("descripcion")
    val descripcion: String?,

    @SerializedName("score")
    val score: Double?
)