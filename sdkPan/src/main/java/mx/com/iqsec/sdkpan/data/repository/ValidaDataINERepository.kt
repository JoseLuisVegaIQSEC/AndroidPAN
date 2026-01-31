package mx.com.iqsec.sdkpan.data.repository

import android.content.Context
import mx.com.iqsec.sdkpan.domain.model.UserINEDataValidationModel
import mx.com.iqsec.sdkpan.domain.util.SDK_ResultValue
import mx.com.iqsec.sdkpan.model.BaseConfig


interface ValidaDataINERepository {
    /**
     * Fetches Challenge Create from a remote data source
     * @param rfc Is rfc of the person
     */
    suspend fun validationDataINE(
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
    ): SDK_ResultValue<UserINEDataValidationModel>
}