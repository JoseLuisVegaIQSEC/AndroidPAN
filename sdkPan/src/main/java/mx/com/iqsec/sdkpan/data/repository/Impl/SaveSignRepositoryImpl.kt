package mx.com.iqsec.sdkpan.data.repository.Impl

import com.google.gson.Gson
import mx.com.iqsec.sdkpan.data.datasource.remote.SaveSignRemoteDataSourceImpl
import mx.com.iqsec.sdkpan.data.dtos.SaveSignDTO
import mx.com.iqsec.sdkpan.data.repository.SaveSignRepository
import mx.com.iqsec.sdkpan.data.util.asDomain
import mx.com.iqsec.sdkpan.domain.base.SDK_MResponseFNDB
import mx.com.iqsec.sdkpan.domain.model.SaveSignModel
import mx.com.iqsec.sdkpan.domain.util.SDK_ResultValue
import mx.com.iqsec.sdkpan.model.BaseConfig

class SaveSignUserRepositoryImpl() : SaveSignRepository {
    private val api = SaveSignRemoteDataSourceImpl()

    override suspend fun saveSign(
        config: BaseConfig,
        firma: String
    ): SDK_ResultValue<SaveSignModel> {
        val response = api.saveSign(
            config,
            firma
        )
        return extractInfo(response)
    }

    private fun extractInfo(
        response: SDK_MResponseFNDB<Any>
    ): SDK_ResultValue<SaveSignModel> {

        return if (response.codigo == 0) {

            val gson = Gson()
            val jsonString = gson.toJson(response.data)

            val procedureModel: SaveSignModel =
                gson.fromJson(jsonString, SaveSignDTO::class.java).asDomain()

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