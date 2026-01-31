package mx.com.iqsec.sdkpan.data.datasource.remote

import mx.com.iqsec.sdkpan.common.constants.SDK_Constants
import mx.com.iqsec.sdkpan.data.network.SDK_ApiService
import mx.com.iqsec.sdkpan.domain.base.SDK_MResponseFNDB
import mx.com.iqsec.sdkpan.model.BaseConfig

internal class SaveSignRemoteDataSourceImpl(
) {
    val api = SDK_ApiService()

    suspend fun saveSign(
        config: BaseConfig,
        firma: String
    ): SDK_MResponseFNDB<Any> {

        val hashMap = HashMap<String, Any>().apply {
            this["firma"] = firma
        }

        return api.executePost(
            "${config.servicesUrl}${SDK_Constants.SAVE_SIGN}",
            hashMap
        )


    }
}