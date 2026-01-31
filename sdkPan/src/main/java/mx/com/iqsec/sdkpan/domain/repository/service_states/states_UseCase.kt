package mx.com.iqsec.sdkpan.domain.repository.service_states

import android.content.Context
import mx.com.iqsec.sdkpan.data.repository.Impl.StatesRepositoryImpl
import mx.com.iqsec.sdkpan.domain.model.StatesModel
import mx.com.iqsec.sdkpan.domain.util.SDK_ResultValue
import mx.com.iqsec.sdkpan.model.BaseConfig

class states_UseCase() {
    suspend operator fun invoke(
        config: BaseConfig,
        context: Context
    ): SDK_ResultValue<List<StatesModel>> {
        return StatesRepositoryImpl().GetStates(
            config,
            context
        )
    }
}