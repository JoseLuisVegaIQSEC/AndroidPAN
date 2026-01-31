package mx.com.iqsec.sdkpan.domain.model

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class UserINEDataValidationModel(
    val tipoSituacionRegistral: String,
    val claveElector: Boolean,
    val anioRegistro: Boolean,
    val apellidoPaterno: Boolean,
    val anioEmision: Boolean,
    val numeroEmisionCredencial: Boolean,
    val nombre: Boolean,
    val curp: Boolean,
    val apellidoMaterno: Boolean,
    val ocr: Boolean
) : Serializable {

    /**
     * Comprueba que existan la credencial y la CURP (tipo de INE válido para ciertos casos).
     */
    fun validateINEtype(): Boolean {
        return this.numeroEmisionCredencial && this.curp
    }

    /**
     * Comprueba si la situación registral es VIGENTE (case-insensitive).
     */
    fun validateValidity(): Boolean {
        return this.tipoSituacionRegistral.equals("VIGENTE", ignoreCase = true)
    }

    /**
     * Valida que todos los campos requeridos sean true y que la INE esté vigente.
     */
    fun isCompleteValidation(): Boolean {
        return validateValidity() &&
                this.claveElector &&
                this.anioRegistro &&
                this.apellidoPaterno &&
                this.anioEmision &&
                this.numeroEmisionCredencial &&
                this.nombre &&
                this.curp &&
                this.apellidoMaterno &&
                this.ocr &&
                this.tipoSituacionRegistral.equals("VIGENTE", ignoreCase = true)
    }
}