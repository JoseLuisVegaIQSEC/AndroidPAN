package mx.com.iqsec.sdkpan.data.repository.Impl

import com.google.gson.Gson
import mx.com.iqsec.sdkpan.data.datasource.remote.ValidateMembershipRemoteDataSourceImpl
import mx.com.iqsec.sdkpan.data.dtos.ValidateMembershipDTO
import mx.com.iqsec.sdkpan.data.repository.ValidateMembershipRepository
import mx.com.iqsec.sdkpan.data.util.asDomain
import mx.com.iqsec.sdkpan.domain.base.SDK_MResponseFNDB
import mx.com.iqsec.sdkpan.domain.model.ValidateMembershipModel
import mx.com.iqsec.sdkpan.domain.util.SDK_ResultValue
import mx.com.iqsec.sdkpan.model.BaseConfig

class ValidateMembershipRepositoryImpl() : ValidateMembershipRepository {
    private val api = ValidateMembershipRemoteDataSourceImpl()

    override suspend fun ValidateMembership(
        referencia: String,
        curp: String,
        config: BaseConfig,
    ): SDK_ResultValue<ValidateMembershipModel> {
        val response = api.ValidateMembership(
            referencia,
            curp,
            config,
        )
        return extractInfo(response)
    }

    private fun extractInfo(
        response: SDK_MResponseFNDB<Any>,
    ): SDK_ResultValue<ValidateMembershipModel> {

        return if (response.codigo == 0) {

            val gson = Gson()
            val jsonString = gson.toJson(response.data)

            val dataModel: ValidateMembershipModel =
                gson.fromJson(jsonString, ValidateMembershipDTO::class.java).asDomain()

            SDK_ResultValue.Success(dataModel)
        } else {
            SDK_ResultValue.Error(response.message, response.codService, response.messageService)
        }
    }
}