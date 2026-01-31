package mx.com.iqsec.sdkpan.data.datasource.remote

import android.content.Context
import mx.com.iqsec.sdkpan.common.constants.SDK_Constants
import mx.com.iqsec.sdkpan.data.network.SDK_ApiService
import mx.com.iqsec.sdkpan.domain.base.SDK_MResponseFNDB
import mx.com.iqsec.sdkpan.model.BaseConfig

internal class SDKStatesRemoteDataSourceImpl(
) {
    val api = SDK_ApiService()

    suspend fun GetStates(
        config: BaseConfig,
        context: Context
    ): SDK_MResponseFNDB<Any> {

        return api.executeGet("${config.servicesUrl}${SDK_Constants.SERVICE_CAT_ESTADOS}")
    }
}