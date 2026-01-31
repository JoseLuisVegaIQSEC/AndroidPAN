package mx.com.iqsec.sdkpan.data.repository

import android.content.Context
import mx.com.iqsec.sdkpan.domain.model.INEModel
import mx.com.iqsec.sdkpan.domain.util.SDK_ResultValue
import mx.com.iqsec.sdkpan.model.BaseConfig

interface INERepository {
    /**
     * Fetches Challenge Create from a remote data source
     * @param frontalBase64 Is base64 image front
     * @param reversoBase64 Is base64 image back
     */
    suspend fun validaINE(
        frontalBase64: String,
        reversoBase64: String,
        config: BaseConfig,
        context: Context
    ): SDK_ResultValue<INEModel>
}