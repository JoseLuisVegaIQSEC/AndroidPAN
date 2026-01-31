package mx.com.iqsec.sdkpan.data.dtos

import com.google.gson.annotations.SerializedName

data class ValidaINEDTO(
    @SerializedName("tipoSituacionRegistral")
    val tipoSituacionRegistral: String,

    @SerializedName("claveElector")
    val claveElector: Boolean,

    @SerializedName("anioRegistro")
    val anioRegistro: Boolean,

    @SerializedName("apellidoPaterno")
    val apellidoPaterno: Boolean,

    @SerializedName("anioEmision")
    val anioEmision: Boolean,

    @SerializedName("numeroEmisionCredencial")
    val numeroEmisionCredencial: Boolean,

    @SerializedName("nombre")
    val nombre: Boolean,

    @SerializedName("curp")
    val curp: Boolean,

    @SerializedName("apellidoMaterno")
    val apellidoMaterno: Boolean,

    @SerializedName("ocr")
    val ocr: Boolean
)
