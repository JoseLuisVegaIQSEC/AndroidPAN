package mx.com.iqsec.sdkpan.data.datasource.remote

import android.content.Context
import mx.com.iqsec.sdkpan.common.constants.SDK_Constants
import mx.com.iqsec.sdkpan.data.network.SDK_ApiService
import mx.com.iqsec.sdkpan.domain.base.SDK_MResponseFNDB
import mx.com.iqsec.sdkpan.model.BaseConfig

internal class SDKMunicipalitiesRemoteDataSourceImpl(
) {
    val api = SDK_ApiService()

    suspend fun GetMunicipios(
        id_estado: Int,
        config: BaseConfig,
        context: Context
    ): SDK_MResponseFNDB<Any> {

        val hashMap = HashMap<String, Any>().apply {
            this["id_estado"] = id_estado
        }

        return api.executePost(
            "${config.servicesUrl}${SDK_Constants.SERVICE_CAT_MUNICIPIOS}",
            hashMap
        )
    }
}