package mx.com.iqsec.sdkpan.domain.repository.save_sign

import mx.com.iqsec.sdkpan.data.repository.Impl.SaveSignUserRepositoryImpl
import mx.com.iqsec.sdkpan.domain.model.SaveSignModel
import mx.com.iqsec.sdkpan.domain.util.SDK_ResultValue
import mx.com.iqsec.sdkpan.model.BaseConfig

class SaveSign_UseCase() {
    suspend operator fun invoke(
        config: BaseConfig,
        firma: String
    ): SDK_ResultValue<SaveSignModel> {
        return SaveSignUserRepositoryImpl().saveSign(
            config,
            firma
        )
    }
}