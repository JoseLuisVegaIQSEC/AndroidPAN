package mx.com.iqsec.sdkpan.data.dtos

import com.google.gson.annotations.SerializedName

data class ValidateMembershipDTO (
    @SerializedName("existe")
    val existe: Boolean,

    @SerializedName("folioAfiliado")
    val folioAfiliado: String?,

    @SerializedName("referencia")
    val referencia: String?
)