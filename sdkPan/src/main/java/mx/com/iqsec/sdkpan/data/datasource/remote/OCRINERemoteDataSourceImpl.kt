package mx.com.iqsec.sdkpan.data.datasource.remote

import android.content.Context
import mx.com.iqsec.sdkpan.common.constants.SDK_Constants
import mx.com.iqsec.sdkpan.data.network.SDK_ApiService
import mx.com.iqsec.sdkpan.domain.base.SDK_MResponseFNDB
import mx.com.iqsec.sdkpan.model.BaseConfig

internal class OCRINERemoteDataSourceImpl(
) {
    val api = SDK_ApiService()

    suspend fun validaINE(
        frontalBase64: String,
        reversoBase64: String,
        config: BaseConfig,
        context: Context
    ): SDK_MResponseFNDB<Any> {

        val hashMap = HashMap<String, Any>().apply {
            this["imagenAnverso"] = frontalBase64
            this["imagenReverso"] = reversoBase64
            this["referencia"] = config.reference
        }

        return api.executePost("${config.servicesUrl}${SDK_Constants.SERVICE_OCR_INE}", hashMap)

    }
}