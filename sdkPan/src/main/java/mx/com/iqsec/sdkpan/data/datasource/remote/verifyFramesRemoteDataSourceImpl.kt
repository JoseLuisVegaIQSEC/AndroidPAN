package mx.com.iqsec.sdkpan.data.datasource.remote

import mx.com.iqsec.sdkpan.common.constants.SDK_Constants
import mx.com.iqsec.sdkpan.data.network.SDK_ApiService
import mx.com.iqsec.sdkpan.domain.base.SDK_MResponseFNDB
import mx.com.iqsec.sdkpan.model.BaseConfig

internal class verifyFramesRemoteDataSourceImpl()  {
    val api = SDK_ApiService()

    suspend fun verifyFrames(
        imageBaseB64: String,
        imagesFramesB64: String,
        config: BaseConfig,
    ): SDK_MResponseFNDB<Any> {

        val hashMap = HashMap<String, Any>().apply {
            this["foto"] = imageBaseB64
            this["credencial"] = imagesFramesB64
            this["referencia"] = config.reference
        }

        return api.executePost("${config.servicesUrl}${SDK_Constants.SERVICE_COMPARE_FACE_INE}", hashMap)
    }
}