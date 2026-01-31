package mx.com.iqsec.sdkpan.data.dtos

import com.google.gson.annotations.SerializedName

class INEDTO (
    @SerializedName("tipo")
    val tipo: String?,

    @SerializedName("subTipo")
    val subTipo: String?,

    @SerializedName("claveElector")
    val claveElector: String?,

    @SerializedName("registro")
    val registro: String?,

    @SerializedName("curp")
    val curp: String?,

    @SerializedName("seccion")
    val seccion: String?,

    @SerializedName("vigencia")
    val vigencia: String?,

    @SerializedName("emision")
    val emision: String?,

    @SerializedName("no_emision")
    val noEmision: String?,

    @SerializedName("sexo")
    val sexo: String?,

    @SerializedName("primerApellido")
    var primerApellido: String?,

    @SerializedName("segundoApellido")
    var segundoApellido: String?,

    @SerializedName("nombres")
    val nombres: String?,

    @SerializedName("calle")
    var calle: String?,

    @SerializedName("colonia")
    var colonia: String?,

    @SerializedName("ciudad")
    val ciudad: String?,

    @SerializedName("fechaNacimiento")
    val fechaNacimiento: String?,

    @SerializedName("folio")
    val folio: Int,

    @SerializedName("mrz")
    val mrz: String?,

    @SerializedName("cic")
    val cic: String?,

    @SerializedName("ocr")
    val ocr: String?,

    @SerializedName("identificadorCiudadano")
    val identificadorCiudadano: String?,

    @SerializedName("referencia")
    val referencia: String?,

    @SerializedName("estado")
    val estado: Int?,

    @SerializedName("descripcion")
    val descripcion: String?
)