package mx.com.iqsec.sdkpan.data.datasource.remote

import android.content.Context
import mx.com.iqsec.sdkpan.common.constants.SDK_Constants
import mx.com.iqsec.sdkpan.data.network.SDK_ApiService
import mx.com.iqsec.sdkpan.domain.base.SDK_MResponseFNDB
import mx.com.iqsec.sdkpan.domain.model.UserINEModel
import mx.com.iqsec.sdkpan.model.BaseConfig
import org.json.JSONObject
import java.io.File

internal class SaveDataUserRemoteDataSourceImpl(
) {
    val api = SDK_ApiService()

    suspend fun saveData(
        userData: UserINEModel,
        config: BaseConfig,
        context: Context
    ): SDK_MResponseFNDB<Any> {

        val hashMap = HashMap<String, Any>().apply {
            this["nombres"] = userData.nombres
            this["primerApellido"] = userData.primerApellido ?: ""
            this["segundoApellido"] = userData.segundoApellido ?: ""
            this["fechaNacimiento"] = userData.fechaNacimiento ?: ""
            this["curp"] = userData.curp ?: ""
            this["claveElector"] = userData.claveElector ?: ""
            this["entidadFederativa"] = userData.state ?: ""
            this["municipio"] = userData.municipio ?: ""
            this["tipo"] = userData.tipo ?: ""
            this["subTipo"] = userData.subTipo ?: ""
            this["sexo"] = userData.sexo ?: ""
            this["seccion"] = userData.seccion ?: ""
            this["localidad"] = userData.municipio ?: ""
            this["emision"] = userData.emision ?: ""
            this["no_emision"] = userData.noEmision ?: ""
            this["vigencia"] = userData.vigencia ?: ""
            this["registro"] = userData.registro ?: ""
            this["calle"] = userData.calle ?: ""
            this["colonia"] = userData.colonia ?: ""
            this["ciudad"] = userData.ciudad ?: ""
            this["folio"] = 0
            this["mrz"] = userData.mrz ?: ""
            this["cic"] = userData.cic ?: ""
            this["ocr"] = userData.ocr ?: ""
            this["identificadorCiudadano"] = userData.identificadorCiudadano ?: ""
            this["correo"] = userData.email
            this["celular"] = userData.phone
            this["facebook"] = userData.socialFacebook
            this["instagram"] = userData.socialInstagram
            this["tiktok"] = userData.socialTiktok
            this["x"] = userData.socialTwitter
            this["areasInteres"] = userData.userInterests.map { it.name }
            this["anverso"] = userData.base64Front ?: ""
            this["reverso"] = userData.base64Reverse ?: ""
            this["referencia"] = config.reference
            this["dialecto"] = userData.speakADialect
            this["escolaridad"] = userData.schooling
            this["rostro"] = userData.fotousuario ?: ""
            this["firma"] = userData.firma ?: ""
            this["perfil"] = "Adherente"
            this["abreviatura"] = userData.cortoState
            this["email"] = userData.email
            this["folioAfiliado"] = ""
            this["no_interior"] = userData.noInterior ?: ""
            this["no_exterior"] = userData.noExterior ?: ""
            this["cp"] = userData.codigoPostal ?: ""
        }

        try {
            // Convertir hashMap a JSON para guardarlo en archivo txt
            val json = JSONObject(hashMap as Map<*, *>).toString()
            val fileName = "hashmap_user_${System.currentTimeMillis()}.txt"
            val file = File(context.filesDir, fileName)
            file.writeText(json)
        } catch (e: Exception) {
            // en caso de fallo al escribir, no romper la ejecución; se puede loggear si se requiere
        }

        return api.executePost(
            "${config.servicesUrl}${SDK_Constants.SERVICE_DATA_USER_INE}",
            hashMap
        )


    }
}