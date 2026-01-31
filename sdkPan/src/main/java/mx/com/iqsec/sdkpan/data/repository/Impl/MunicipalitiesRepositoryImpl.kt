package mx.com.iqsec.sdkpan.data.repository.Impl

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import mx.com.iqsec.sdkpan.data.datasource.remote.SDKMunicipalitiesRemoteDataSourceImpl
import mx.com.iqsec.sdkpan.data.dtos.MunicipalitiesDTO
import mx.com.iqsec.sdkpan.data.repository.MunicipalitiesRepository
import mx.com.iqsec.sdkpan.data.util.asListMunicipalities
import mx.com.iqsec.sdkpan.domain.base.SDK_MResponseFNDB
import mx.com.iqsec.sdkpan.domain.model.MunicipalitiesModel
import mx.com.iqsec.sdkpan.domain.util.SDK_ResultValue
import mx.com.iqsec.sdkpan.model.BaseConfig

class MunicipalitiesRepositoryImpl() : MunicipalitiesRepository {
    private val api = SDKMunicipalitiesRemoteDataSourceImpl()

    override suspend fun GetMunicipalities(
        id_estado: Int,
        config: BaseConfig,
        context: Context
    ): SDK_ResultValue<List<MunicipalitiesModel>> {
        val response = api.GetMunicipios(
            id_estado,
            config,
            context
        )
        return extractInfo(response, context)
    }

    private fun extractInfo(
        response: SDK_MResponseFNDB<Any>,
        context: Context
    ): SDK_ResultValue<List<MunicipalitiesModel>> {

        return if (response.codigo == 0) {

            if (response.data is List<*>) {

                val gson = Gson()
                val jsonString = gson.toJson(response.data)
                val listSignedDocumentsModel: List<MunicipalitiesDTO> =
                    gson.fromJson(jsonString, object : TypeToken<List<MunicipalitiesDTO>>() {}.type)

                val signedDocumentsModel: ArrayList<MunicipalitiesModel> =
                    listSignedDocumentsModel.asListMunicipalities()

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