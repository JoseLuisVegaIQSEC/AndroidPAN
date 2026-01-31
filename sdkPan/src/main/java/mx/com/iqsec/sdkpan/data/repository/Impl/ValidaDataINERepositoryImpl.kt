package mx.com.iqsec.sdkpan.data.repository.Impl

import android.content.Context
import com.google.gson.Gson
import mx.com.iqsec.sdkpan.common.constants.SDK_Constants
import mx.com.iqsec.sdkpan.data.datasource.remote.ValidaDataByINERemoteDataSourceImpl
import mx.com.iqsec.sdkpan.data.di.manager.SDK_SPManager
import mx.com.iqsec.sdkpan.data.dtos.ValidaINEDTO
import mx.com.iqsec.sdkpan.data.repository.ValidaDataINERepository
import mx.com.iqsec.sdkpan.data.util.asDomain
import mx.com.iqsec.sdkpan.domain.base.SDK_MResponseFNDB
import mx.com.iqsec.sdkpan.domain.model.UserINEDataValidationModel
import mx.com.iqsec.sdkpan.domain.util.SDK_ResultValue
import mx.com.iqsec.sdkpan.model.BaseConfig

class ValidaDataINERepositoryImpl() : ValidaDataINERepository {
    private val api = ValidaDataByINERemoteDataSourceImpl()

    override suspend fun validationDataINE(
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
    ): SDK_ResultValue<UserINEDataValidationModel> {
        val response = api.validaDataINE(
            cic,
            nombre,
            paterno,
            materno,
            claveElector,
            numeroEmision,
            curp,
            anioRegistro,
            anioEmision,
            latitud,
            longitud,
            ocr,
            codigopostal,
            ciudad,
            estado,
            config,
            context
        )
        return extractInfo(response, context)
    }

    private fun extractInfo(
        response: SDK_MResponseFNDB<Any>,
        context: Context
    ): SDK_ResultValue<UserINEDataValidationModel> {
        return if (response.codigo == 0) {

            val gson = Gson()
            val jsonString = gson.toJson(response.data)

            val sdk_SPM = SDK_SPManager(context)
            sdk_SPM.saveString(SDK_Constants.RES_SERVICE_INE_VALIDATION, jsonString)

            val procedureModel: UserINEDataValidationModel =
                gson.fromJson(jsonString, ValidaINEDTO::class.java).asDomain()

            SDK_ResultValue.Success(procedureModel)
        } else {
            SDK_ResultValue.Error(response.message, response.codService, response.messageService)
        }
    }
}