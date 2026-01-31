package mx.com.iqsec.sdkpan.data.datasource.remote

import mx.com.iqsec.sdkpan.common.constants.SDK_Constants
import mx.com.iqsec.sdkpan.data.network.SDK_ApiService
import mx.com.iqsec.sdkpan.domain.base.SDK_MResponseFNDB
import mx.com.iqsec.sdkpan.model.BaseConfig

class ValidaAntispoffingFNRemoteDataSource {
    val api = SDK_ApiService()

    suspend fun ValidaAntispoofFN(
        imgs_base64: ArrayList<String>,
        config: BaseConfig
    ): SDK_MResponseFNDB<Any> {
        val hashMap = HashMap<String, Any>().apply {
            this["imgs_base64"] = imgs_base64
            this["referencia"] = config.reference
        }

        return api.executePost("${config.servicesUrl}${SDK_Constants.SERVICE_ANTISPOFFING}", hashMap)
    }
}