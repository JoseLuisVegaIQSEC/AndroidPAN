package mx.com.iqsec.sdkpan.data.repository.Impl

import com.google.gson.Gson
import mx.com.iqsec.sdkpan.data.datasource.remote.ValidaAntispoffingFNRemoteDataSource
import mx.com.iqsec.sdkpan.data.dtos.AntispoffingDTO
import mx.com.iqsec.sdkpan.data.repository.ValidaAntispoffingFNRepository
import mx.com.iqsec.sdkpan.data.util.asDomain
import mx.com.iqsec.sdkpan.domain.base.SDK_MResponseFNDB
import mx.com.iqsec.sdkpan.domain.model.AntispoffingDataModel
import mx.com.iqsec.sdkpan.domain.model.AntispoffingModel
import mx.com.iqsec.sdkpan.domain.util.SDK_ResultValue
import mx.com.iqsec.sdkpan.model.BaseConfig

class ValidaAntispoffingIFNepositoryImpl() : ValidaAntispoffingFNRepository {
    private val api = ValidaAntispoffingFNRemoteDataSource()

    override suspend fun ValidaAntispoffiingFN(
        imgs_base64: ArrayList<String>,
        config: BaseConfig
    ): SDK_ResultValue<AntispoffingDataModel> {
        val response = api.ValidaAntispoofFN(imgs_base64, config)
        return extractInfo(response)
    }

    private fun extractInfo(response: SDK_MResponseFNDB<Any>): SDK_ResultValue<AntispoffingDataModel> {

        return if (response.codigo == 0) {

            if (response.data is Map<*, *>) {

                val gson = Gson()
                val jsonString = gson.toJson(response.data)
                val procedureModel: AntispoffingModel =
                    gson.fromJson(jsonString, AntispoffingDTO::class.java).asDomain()

                SDK_ResultValue.Success(procedureModel.data)

            } else {
                SDK_ResultValue.Error(
                    response.message,
                    response.codService,
                    response.messageService
                )
            }
        } else {
            SDK_ResultValue.Error(
                response.message,
                response.codService,
                response.messageService
            )
        }
    }
}