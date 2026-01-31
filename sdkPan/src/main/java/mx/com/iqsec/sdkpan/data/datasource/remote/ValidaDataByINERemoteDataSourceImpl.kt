package mx.com.iqsec.sdkpan.data.datasource.remote

import android.content.Context
import com.google.gson.Gson
import mx.com.iqsec.sdkpan.common.constants.SDK_Constants
import mx.com.iqsec.sdkpan.data.di.manager.SDK_SPManager
import mx.com.iqsec.sdkpan.data.network.SDK_ApiService
import mx.com.iqsec.sdkpan.domain.base.SDK_MResponseFNDB
import mx.com.iqsec.sdkpan.model.BaseConfig

internal class ValidaDataByINERemoteDataSourceImpl() {
    val api = SDK_ApiService()

    suspend fun validaDataINE(
        cic: String,
        nombre: String,
        paterno: String,
        materno: String,
        claveElector: String,
        numeroEmision: String,
        curp: String,
        anioRegistro: String,
        anioEmision: String,
        latitud: Double,
        longitud: Double,
        ocr: String,
        codigopostal: String,
        ciudad: String,
        estado: Int,
        config: BaseConfig,
        context: Context
    ): SDK_MResponseFNDB<Any> {

        val hashMap = HashMap<String, Any>().apply {
            this["nombre"] = nombre
            this["paterno"] = paterno
            this["materno"] = materno
            this["claveElector"] = claveElector
            this["numeroEmision"] = numeroEmision
            this["curp"] = curp
            this["anioRegistro"] = anioRegistro
            this["anioEmision"] = anioEmision
            this["latitud"] = latitud
            this["longitud"] = longitud
            this["cic"] = cic
            this["ocr"] = ocr
            this["codigopostal"] = codigopostal
            this["ciudad"] = ciudad
            this["estado"] = estado
        }

        val gson = Gson()
        val sdk_SPM = SDK_SPManager(context)
        // Guardar la petición en SharedPreferences (si se requiere), serializando el mismo mapa
        sdk_SPM.saveString(SDK_Constants.SEND_SERVICE_INE_VALIDATION, gson.toJson(hashMap))

        return api.executePost("${config.servicesUrl}${SDK_Constants.SERVICE_VALIDATE_INE}", hashMap)
    }
}