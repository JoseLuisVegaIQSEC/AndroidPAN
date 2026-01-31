package mx.com.iqsec.sdkpan.data.dtos

import com.google.gson.annotations.SerializedName

class DataUserDTO (
    @SerializedName("estado")
    val estado: Int?,

    @SerializedName("descripcion")
    val descripcion: String?,

    @SerializedName("folio")
    val folio: Int?,

    @SerializedName("identificacion")
    val identificacion: String?,

    @SerializedName("nombre")
    val nombre: String?,

    @SerializedName("primerApellido")
    val primerApellido: String?,

    @SerializedName("segundoApellido")
    val segundoApellido: String?,

    @SerializedName("municipio")
    val municipio: String?,

    @SerializedName("entidadFederativa")
    val entidadFederativa: String?,

    @SerializedName("perfil")
    val perfil: String?,

    @SerializedName("vencimiento")
    val vencimiento: String?,

    @SerializedName("token")
    val token: String?,

    @SerializedName("rostro")
    val rostro: String?,

    @SerializedName("qr")
    val qr: String?,

    @SerializedName("registro")
    val registro: String?,

    @SerializedName("folioAfiliado")
    val folioAfiliado: String?
)