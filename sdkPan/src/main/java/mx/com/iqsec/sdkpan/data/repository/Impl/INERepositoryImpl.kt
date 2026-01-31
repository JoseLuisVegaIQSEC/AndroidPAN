package mx.com.iqsec.sdkpan.data.repository.Impl

import android.content.Context
import com.google.gson.Gson
import mx.com.iqsec.sdkpan.common.constants.SDK_Constants
import mx.com.iqsec.sdkpan.data.datasource.remote.OCRINERemoteDataSourceImpl
import mx.com.iqsec.sdkpan.data.di.manager.SDK_SPManager
import mx.com.iqsec.sdkpan.data.dtos.INEDTO
import mx.com.iqsec.sdkpan.data.repository.INERepository
import mx.com.iqsec.sdkpan.data.util.asDomain
import mx.com.iqsec.sdkpan.domain.base.SDK_MResponseFNDB
import mx.com.iqsec.sdkpan.domain.model.INEModel
import mx.com.iqsec.sdkpan.domain.util.SDK_ResultValue
import mx.com.iqsec.sdkpan.model.BaseConfig

class INERepositoryImpl() : INERepository {
    private val api = OCRINERemoteDataSourceImpl()

    override suspend fun validaINE(
        frontalBase64: String,
        reversoBase64: String,
        config: BaseConfig,
        context: Context
    ): SDK_ResultValue<INEModel> {
        val response = api.validaINE(
            frontalBase64,
            reversoBase64,
            config,
            context
        )
        return extractInfo(response, context)
    }

    private fun extractInfo(
        response: SDK_MResponseFNDB<Any>,
        context: Context
    ): SDK_ResultValue<INEModel> {

        return if (response.codigo == 0) {

            val gson = Gson()
            val jsonString = gson.toJson(response.data)

            val sdk_SPM = SDK_SPManager(context)
            sdk_SPM.saveString(SDK_Constants.RES_OCR_VALIDATION, jsonString)

            val procedureModel: INEModel =
                gson.fromJson(jsonString, INEDTO::class.java).asDomain()

            if (procedureModel.estado == 0) {
                SDK_ResultValue.Success(procedureModel)
            } else {
                SDK_ResultValue.Error(
                    Pair("", procedureModel.descripcion.toString()),
                    response.codService,
                    response.messageService
                )
            }
        } else {
            SDK_ResultValue.Error(response.message, response.codService, response.messageService)
        }
    }
}