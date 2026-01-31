package mx.com.iqsec.sdkpan.data.repository.Impl

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import mx.com.iqsec.sdkpan.data.datasource.remote.SDKStatesRemoteDataSourceImpl
import mx.com.iqsec.sdkpan.data.dtos.StatesDTO
import mx.com.iqsec.sdkpan.data.repository.StatesRepository
import mx.com.iqsec.sdkpan.data.util.asListStates
import mx.com.iqsec.sdkpan.domain.base.SDK_MResponseFNDB
import mx.com.iqsec.sdkpan.domain.model.StatesModel
import mx.com.iqsec.sdkpan.domain.util.SDK_ResultValue
import mx.com.iqsec.sdkpan.model.BaseConfig

class StatesRepositoryImpl() : StatesRepository {
    private val api = SDKStatesRemoteDataSourceImpl()

    override suspend fun GetStates(
        config: BaseConfig,
        context: Context
    ): SDK_ResultValue<List<StatesModel>> {
        val response = api.GetStates(
            config,
            context
        )
        return extractInfo(response, context)
    }

    private fun extractInfo(
        response: SDK_MResponseFNDB<Any>,
        context: Context
    ): SDK_ResultValue<List<StatesModel>> {

        return if (response.codigo == 0) {

            if (response.data is List<*>) {

                val gson = Gson()
                val jsonString = gson.toJson(response.data)
                val listSignedDocumentsModel: List<StatesDTO> =
                    gson.fromJson(jsonString, object : TypeToken<List<StatesDTO>>() {}.type)

                val signedDocumentsModel: ArrayList<StatesModel> =
                    listSignedDocumentsModel.asListStates()

                if (signedDocumentsModel.isNotEmpty()) {
                    SDK_ResultValue.Success(signedDocumentsModel)
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

        } else {
            SDK_ResultValue.Error(response.message, response.codService, response.messageService)
        }
    }
}