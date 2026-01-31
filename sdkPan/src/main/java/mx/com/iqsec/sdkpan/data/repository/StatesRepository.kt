package mx.com.iqsec.sdkpan.data.repository

import android.content.Context
import mx.com.iqsec.sdkpan.domain.model.StatesModel
import mx.com.iqsec.sdkpan.domain.util.SDK_ResultValue
import mx.com.iqsec.sdkpan.model.BaseConfig

interface StatesRepository {
    /**
     * Fetches Challenge Create from a remote data source
     */
    suspend fun GetStates(
        config: BaseConfig,
        context: Context
    ): SDK_ResultValue<List<StatesModel>>
}