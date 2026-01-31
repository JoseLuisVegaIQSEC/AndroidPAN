package mx.com.iqsec.sdkpan.data.dtos

import com.google.gson.annotations.SerializedName

class SaveSignDTO (
    @SerializedName("estado")
    val estado: Int?,

    @SerializedName("descripcion")
    val descripcion: String?,

    @SerializedName("folio")
    val folio: Int?,

    @SerializedName("referencia")
    val referencia: String?
)