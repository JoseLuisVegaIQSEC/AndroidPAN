package mx.com.iqsec.sdkpan.domain.repository.valida_antispoffing_fn

import mx.com.iqsec.sdkpan.data.repository.Impl.ValidaAntispoffingIFNepositoryImpl
import mx.com.iqsec.sdkpan.domain.model.AntispoffingDataModel
import mx.com.iqsec.sdkpan.domain.util.SDK_ResultValue
import mx.com.iqsec.sdkpan.model.BaseConfig

class ValidaAntispoffingFNUseCase() {
    suspend operator fun invoke(
        imgs_base64: ArrayList<String>,
        config: BaseConfig
    ): SDK_ResultValue<AntispoffingDataModel> {
        return ValidaAntispoffingIFNepositoryImpl().ValidaAntispoffiingFN(
            imgs_base64,
            config
        )
    }
}