package mx.com.iqsec.sdkpan.data.repository

import android.content.Context
import mx.com.iqsec.sdkpan.domain.model.VerifyFramesModel
import mx.com.iqsec.sdkpan.domain.util.SDK_ResultValue
import mx.com.iqsec.sdkpan.model.BaseConfig


interface VerifyFramesRepository {
    /**
     * Fetches Challenge Create from a remote data source
     * @param imageBaseB64 Is base64 image
     * @param imagesFramesB64 Is array base64 image
     */
    suspend fun verifyFrames(
        imageBaseB64: String,
        imagesFramesB64: String,
        config: BaseConfig,
        context: Context
    ): SDK_ResultValue<VerifyFramesModel>
}