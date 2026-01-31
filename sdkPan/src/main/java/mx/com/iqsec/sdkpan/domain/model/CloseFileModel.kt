package mx.com.iqsec.sdkpan.domain.model

import androidx.annotation.Keep
import java.io.Serializable

@Keep
class CloseFileModel(
    val estado: Int?,
    val descripcion: String?,
    val folio: Int?,
    val identificacion: String?,
    val nombre: String?,
    val primerApellido: String?,
    val segundoApellido: String?,
    val municipio: String?,
    val entidadFederativa: String?,
    val perfil: String?,
    val vencimiento: String?,
    val token: String?,
    val rostro: String?,
    val qr: String?,
    val registro: String?,
    val folioAfiliado: String?,
) : Serializable