package mx.com.iqsec.sdkpan.data.datasource.remote

import mx.com.iqsec.sdkpan.common.constants.SDK_Constants
import mx.com.iqsec.sdkpan.data.network.SDK_ApiService
import mx.com.iqsec.sdkpan.domain.base.SDK_MResponseFNDB
import mx.com.iqsec.sdkpan.model.BaseConfig

internal class ValidateMembershipRemoteDataSourceImpl(
) {
    val api = SDK_ApiService()

    suspend fun ValidateMembership(
        referencia: String,
        curp: String,
        config: BaseConfig,
    ): SDK_MResponseFNDB<Any> {

        val hashMap = HashMap<String, Any>().apply {
            this["referencia"] = referencia
            this["curp"] = curp
        }

        return api.executePost(
            "${config.servicesUrl}${SDK_Constants.SERVICE_VALIDATE_MEMBERSHIP}",
            hashMap
        )
    }
}