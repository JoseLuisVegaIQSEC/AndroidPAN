package mx.com.iqsec.sdkpan.domain.model

import androidx.annotation.Keep
import mx.com.iqsec.sdkpan.model.Minterest
import java.io.Serializable

@Keep
data class UserINEModel(
    var tipo: String? = "",
    var subTipo: String? = "",
    var claveElector: String? = "",
    var registro: String? = "",
    var curp: String? = "",
    var seccion: String? = "",
    var vigencia: String? = "",
    var emision: String? = "",
    var noEmision: String? = "",
    var sexo: String? = "",
    var primerApellido: String? = "",
    var segundoApellido: String? = "",
    var nombres: String = "",
    var calle: String? = "",
    var noInterior: String? = "",
    var noExterior: String? = "",
    var colonia: String? = "",
    var ciudad: String? = "",
    var fechaNacimiento: String? = "",
    var folio: Int = 0,
    var folioAfiliado: String? = "",
    var mrz: String? = "",
    var cic: String? = "",
    var ocr: String? = "",
    var identificadorCiudadano: String? = "",
    var firma: String? = "",
    var fotousuario: String? = "",
    var entidadFederativa: String? = "",
    var municipio: String? = "",
    var codigoPostal: String? = "",
    var localidad: String? = "",
    var dataCredentialOk: Boolean = false,
    var curpVerified: Boolean = false,
    var base64Front: String? = "",
    var base64Reverse: String? = "",
    var codState: String = "",
    var state: String = "",
    var cortoState: String = "",
    var codMunicipaly: String = "",
    var email: String = "",
    var phone: String = "",
    var schooling: String = "",
    var speakADialect: String = "",
    var userInterests: ArrayList<Minterest> = ArrayList(),
    var socialFacebook: String = "",
    var socialInstagram: String = "",
    var socialTiktok: String = "",
    var socialTwitter: String = "",
    var referencia: String = "",
    var perfil: String = "",
    var credentiaVigency: String = ""

) : Serializable {
    fun buildName(): String {
        return this.nombres.plus(" ").plus(this.primerApellido).plus(" ").plus(this.segundoApellido)
    }

    fun buildNameVideo(): String {
        return this.nombres.plus("_").plus(this.primerApellido).plus("_").plus(this.segundoApellido)
    }

    fun validateData(): Boolean {
        return (!this.claveElector.equals("") && !this.curp.equals("") && !this.registro.equals(
            ""
        ) && !this.emision.equals(""))
    }

    fun cleanCalle(): String {
        if (this.calle.isNullOrEmpty()) return ""
        val bloques = this.calle.toString().trim().split(" ")
        val direccionLimpia = bloques.filter { !it.matches(Regex("^\\d+$")) }
        return direccionLimpia.joinToString(" ")
    }

    fun cleanColonia(): String {
        if (this.colonia.isNullOrEmpty()) return ""
        val bloques = this.colonia.toString().trim().split(" ")
        val direccionLimpia = bloques.filter { !it.matches(Regex("^\\d+$")) }
        return direccionLimpia.joinToString(" ")
    }

    fun calculateCodigoPostal(): String? {
        if (codigoPostal.toString().isNotEmpty()) return this.codigoPostal

        var codePostal = ""
        val regex = Regex("\\d{5}")
        codePostal = regex.find(this.colonia.toString())?.value ?: ""

        if (codePostal.isEmpty())
            codePostal = regex.find(this.ciudad.toString())?.value ?: ""

        return codePostal
    }

    fun calculateNoInterior(): String {
        if (noInterior.isNullOrEmpty().not()) return noInterior.toString()

        if (calle.isNullOrEmpty()) return ""
        val bloques = calle.toString().trim().split(" ")
        val numeros = bloques.filter { it.matches(Regex("^\\d+$")) }
        return if (numeros.size == 2) numeros.last() else ""
    }

    fun calculateNoExterior(): String {
        if (noExterior.isNullOrEmpty().not()) return noExterior.toString()

        if (calle.isNullOrEmpty()) return ""
        val bloques = calle.toString().trim().split(" ")
        val numeros = bloques.filter { it.matches(Regex("^\\d+$")) }
        return when {
            numeros.size > 1 -> numeros[numeros.size - 2]
            numeros.size == 1 -> numeros[0]
            else -> ""
        }
    }

    fun cleanDataMembership() {
        this.tipo = ""
        this.subTipo = ""
        this.claveElector = ""
        this.registro = ""
        this.curp = ""
        this.seccion = ""
        this.vigencia = ""
        this.emision = ""
        this.noEmision = ""
        this.sexo = ""
        this.primerApellido = ""
        this.segundoApellido = ""
        this.nombres = ""
        this.calle = ""
        this.noInterior = ""
        this.noExterior = ""
        this.colonia = ""
        this.ciudad = ""
        this.fechaNacimiento = ""
        this.folio = 0
        this.folioAfiliado = ""
        this.mrz = ""
        this.cic = ""
        this.ocr = ""
        this.identificadorCiudadano = ""
        this.firma = ""
        this.fotousuario = ""
        this.entidadFederativa = ""
        this.municipio = ""
        this.codigoPostal = ""
        this.localidad = ""
        this.dataCredentialOk = false
        this.curpVerified = false
        this.base64Front = ""
        this.base64Reverse = ""
        this.codState = ""
        this.state = ""
        this.cortoState = ""
        this.codMunicipaly = ""
        this.email = ""
        this.phone = ""
        this.schooling = ""
        this.speakADialect = ""
        this.userInterests = ArrayList()
        this.socialFacebook = ""
        this.socialInstagram = ""
        this.socialTiktok = ""
        this.socialTwitter = ""
        this.referencia = ""
        this.credentiaVigency = ""
    }
}