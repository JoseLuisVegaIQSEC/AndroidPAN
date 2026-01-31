package mx.com.iqsec.sdkpan.domain.model

import androidx.annotation.Keep
import java.io.Serializable

@Keep
class INEModel(
    val estado: Int?,
    val descripcion: String?,
    val tipo: String?,
    val subTipo: String?,
    val claveElector: String?,
    val registro: String?,
    val curp: String?,
    val seccion: String?,
    val vigencia: String?,
    val emision: String?,
    val noEmision: String?,
    val sexo: String?,
    var primerApellido: String?,
    var segundoApellido: String?,
    val nombres: String?,
    var calle: String?,
    var colonia: String?,
    val ciudad: String?,
    val fechaNacimiento: String?,
    val folio: Int,
    val mrz: String?,
    val cic: String?,
    val ocr: String?,
    val identificadorCiudadano: String?,
    val referencia: String?
) : Serializable {
    fun isOCRValid(): Boolean {
        return nombres.isNullOrEmpty().not()
    }

    fun verifySameCredential(): Boolean {
        if (mrz.isNullOrEmpty() || primerApellido.isNullOrEmpty()) return false

        return mrz.contains(primerApellido!!, ignoreCase = true)
    }
}