package mx.com.iqsec.sdkpan.data.repository.Impl

import com.google.gson.Gson
import mx.com.iqsec.sdkpan.data.datasource.remote.CloseFileRemoteDataSourceImpl
import mx.com.iqsec.sdkpan.data.dtos.CloseFileDTO
import mx.com.iqsec.sdkpan.data.repository.CloseFileRepository
import mx.com.iqsec.sdkpan.data.util.asDomain
import mx.com.iqsec.sdkpan.domain.base.SDK_MResponseFNDB
import mx.com.iqsec.sdkpan.domain.model.CloseFileModel
import mx.com.iqsec.sdkpan.domain.model.UserINEModel
import mx.com.iqsec.sdkpan.domain.util.SDK_ResultValue
import mx.com.iqsec.sdkpan.model.BaseConfig

class CloseFileRepositoryImpl() : CloseFileRepository {
    private val api = CloseFileRemoteDataSourceImpl()

    override suspend fun CloseFile(
        userData: UserINEModel,
        config: BaseConfig,
    ): SDK_ResultValue<CloseFileModel> {
        val response = api.CloseFile(
            userData,
            config,
        )
        return extractInfo(response)
    }

    private fun extractInfo(
        response: SDK_MResponseFNDB<Any>,
    ): SDK_ResultValue<CloseFileModel> {

        return if (response.codigo == 0) {

            val gson = Gson()
            val jsonString = gson.toJson(response.data)

            val dataModel: CloseFileModel =
                gson.fromJson(jsonString, CloseFileDTO::class.java).asDomain()

            if (dataModel.estado == 0) {
                SDK_ResultValue.Success(dataModel)
            } else {
                SDK_ResultValue.Error(
                    Pair("", dataModel.descripcion.toString()),
                    response.codService,
                    response.messageService
                )
            }
        } else {
            SDK_ResultValue.Error(response.message, response.codService, response.messageService)
        }
    }
}