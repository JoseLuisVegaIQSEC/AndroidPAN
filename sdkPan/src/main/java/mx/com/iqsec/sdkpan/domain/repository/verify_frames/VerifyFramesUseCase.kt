package mx.com.iqsec.sdkpan.domain.repository.verify_frames

import android.content.Context
import mx.com.iqsec.sdkpan.data.repository.Impl.VerifyFramesRepositoryImpl
import mx.com.iqsec.sdkpan.domain.model.VerifyFramesModel
import mx.com.iqsec.sdkpan.domain.util.SDK_ResultValue
import mx.com.iqsec.sdkpan.model.BaseConfig

class VerifyFramesUseCase()  {
    suspend operator fun invoke(
        imageBaseB64: String,
        imagesFramesB64: String,
        config: BaseConfig,
        context: Context
    ): SDK_ResultValue<VerifyFramesModel> {
        return VerifyFramesRepositoryImpl().verifyFrames(
            imageBaseB64,
            imagesFramesB64,
            config,
            context
        )
    }
}