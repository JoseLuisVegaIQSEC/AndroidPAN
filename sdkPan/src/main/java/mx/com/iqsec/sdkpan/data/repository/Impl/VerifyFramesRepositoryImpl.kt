package mx.com.iqsec.sdkpan.data.repository.Impl

import android.content.Context
import com.google.gson.Gson
import mx.com.iqsec.sdkpan.data.datasource.remote.verifyFramesRemoteDataSourceImpl
import mx.com.iqsec.sdkpan.data.dtos.VerifyFramesDTO
import mx.com.iqsec.sdkpan.data.repository.VerifyFramesRepository
import mx.com.iqsec.sdkpan.data.util.asDomain
import mx.com.iqsec.sdkpan.domain.base.SDK_MResponseFNDB
import mx.com.iqsec.sdkpan.domain.model.VerifyFramesModel
import mx.com.iqsec.sdkpan.domain.util.SDK_ResultValue
import mx.com.iqsec.sdkpan.model.BaseConfig

class VerifyFramesRepositoryImpl() : VerifyFramesRepository {
    private val api = verifyFramesRemoteDataSourceImpl()

    override suspend fun verifyFrames(
        imageBaseB64: String,
        imagesFramesB64: String,
        config: BaseConfig,
        context: Context
    ): SDK_ResultValue<VerifyFramesModel> {
        val response = api.verifyFrames(
            imageBaseB64,
            imagesFramesB64,
            config,
        )
        return extractInfo(response)
    }

    private fun extractInfo(response: SDK_MResponseFNDB<Any>): SDK_ResultValue<VerifyFramesModel> {
        return if (response.codigo == 0) {

            val gson = Gson()
            val jsonString = gson.toJson(response.data)
            val procedureModel: VerifyFramesModel =
                gson.fromJson(jsonString, VerifyFramesDTO::class.java).asDomain()

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