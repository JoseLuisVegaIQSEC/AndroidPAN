package mx.com.iqsec.sdkpan.data.dtos

import com.google.gson.annotations.SerializedName

class MunicipalitiesDTO (
    @SerializedName("id_municipio")
    val id_municipio: Int,

    @SerializedName("nombre")
    val nombre: String?
)