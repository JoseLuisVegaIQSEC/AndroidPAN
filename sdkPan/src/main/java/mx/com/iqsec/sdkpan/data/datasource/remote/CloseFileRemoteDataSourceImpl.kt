package mx.com.iqsec.sdkpan.data.datasource.remote

import mx.com.iqsec.sdkpan.common.constants.SDK_Constants
import mx.com.iqsec.sdkpan.data.network.SDK_ApiService
import mx.com.iqsec.sdkpan.domain.base.SDK_MResponseFNDB
import mx.com.iqsec.sdkpan.domain.model.UserINEModel
import mx.com.iqsec.sdkpan.model.BaseConfig

internal class CloseFileRemoteDataSourceImpl(
) {
    val api = SDK_ApiService()

    suspend fun CloseFile(
        userData: UserINEModel,
        config: BaseConfig,
    ): SDK_MResponseFNDB<Any> {

        val hashMap = HashMap<String, Any>().apply {
            this["vigencia"] = userData.credentiaVigency
            this["perfil"] = userData.perfil
            this["referencia"] = userData.referencia
            this["abreviatura"] = userData.cortoState
            this["email"] = userData.email
        }

        return api.executePost("${config.servicesUrl}${SDK_Constants.SERVICE_CLOSE_FILE}", hashMap)

    }
}