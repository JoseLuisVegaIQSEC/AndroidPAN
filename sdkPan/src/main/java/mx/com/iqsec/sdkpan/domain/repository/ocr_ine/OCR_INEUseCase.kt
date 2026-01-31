package mx.com.iqsec.sdkpan.domain.repository.ocr_ine

import android.content.Context
import mx.com.iqsec.sdkpan.data.repository.Impl.INERepositoryImpl
import mx.com.iqsec.sdkpan.domain.model.INEModel
import mx.com.iqsec.sdkpan.domain.util.SDK_ResultValue
import mx.com.iqsec.sdkpan.model.BaseConfig

class OCR_INEUseCase() {
    suspend operator fun invoke(
        frontalBase64: String,
        reversoBase64: String,
        config: BaseConfig,
        context: Context
    ): SDK_ResultValue<INEModel> {
        return INERepositoryImpl().validaINE(
            frontalBase64,
            reversoBase64,
            config,
            context
        )
    }
}