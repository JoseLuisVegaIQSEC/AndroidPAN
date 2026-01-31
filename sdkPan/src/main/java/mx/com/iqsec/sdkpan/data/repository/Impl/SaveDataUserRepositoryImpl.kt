package mx.com.iqsec.sdkpan.data.repository.Impl

import android.content.Context
import com.google.gson.Gson
import mx.com.iqsec.sdkpan.data.datasource.remote.SaveDataUserRemoteDataSourceImpl
import mx.com.iqsec.sdkpan.data.dtos.DataUserDTO
import mx.com.iqsec.sdkpan.data.repository.SaveDataUserRepository
import mx.com.iqsec.sdkpan.data.util.asDomain
import mx.com.iqsec.sdkpan.domain.base.SDK_MResponseFNDB
import mx.com.iqsec.sdkpan.domain.model.DataUserModel
import mx.com.iqsec.sdkpan.domain.model.UserINEModel
import mx.com.iqsec.sdkpan.domain.util.SDK_ResultValue
import mx.com.iqsec.sdkpan.model.BaseConfig

class SaveDataUserRepositoryImpl() : SaveDataUserRepository {
    private val api = SaveDataUserRemoteDataSourceImpl()

    override suspend fun saveData(
        userData: UserINEModel,
        config: BaseConfig,
        context: Context
    ): SDK_ResultValue<DataUserModel> {
        val response = api.saveData(
            userData,
            config,
            context
        )
        return extractInfo(response)
    }

    private fun extractInfo(
        response: SDK_MResponseFNDB<Any>
    ): SDK_ResultValue<DataUserModel> {

        return if (response.codigo == 0) {

            val gson = Gson()
            val jsonString = gson.toJson(response.data)

            val procedureModel: DataUserModel =
                gson.fromJson(jsonString, DataUserDTO::class.java).asDomain()

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