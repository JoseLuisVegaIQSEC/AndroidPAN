package mx.com.iqsec.sdkpan.data.dtos

import com.google.gson.annotations.SerializedName

class StatesDTO (
    @SerializedName("id_estado")
    val id_estado: Int,

    @SerializedName("nombre")
    val nombre: String?,

    @SerializedName("abreviatura")
    val abreviatura: String?
)